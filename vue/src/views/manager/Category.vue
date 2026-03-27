<template>
  <div>

    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.name" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入分类名称查询"></el-input>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" plain @click="handleAdd">新增</el-button>
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

<!--    表头-->
    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="name" label="分类名称" />
        <el-table-column label="操作" width="100" fixed="right">
          <template v-slot="scope">
            <el-button type="primary" circle :icon="Edit" @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev,pager,next"  :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>

<!--    输入表数据-->
    <el-dialog title="商品分类" v-model="data.formVisible" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="70px" style="padding:20px">
        <el-form-item prop="name" label="分类名称">
          <el-input v-model="data.form.name" placeholder="请输入分类名称"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span  class="dialog-footer">
         <el-button @click="data.formVisible=false">取消</el-button>
         <el-button type="primary"  @click="save">确定</el-button><!--确认后需要连接后端交互信息，在这之前需要进行数据效验-->
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import {reactive} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete,Edit} from "@element-plus/icons-vue";
import request from "@/utils/request.js";

const baseUrl=import.meta.env.VITE_BASE_URL

const handleFileUpload=(res) =>{
  data.form.avatar=res.data
}

const data=reactive({
  formVisible:false, //弹出对话框el-daiglog，初始为不弹出
  form:{},//数据存储在from中，form联通了Account类的属性,先清空表单
  tableData:[],
  pageNum:1,
  pageSize:5,
  total:0,
  name:null,
  ids:[]
})

//查询表数据的接口
const load=()=>{
  request.get('/category/selectPage',{
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name
    }
  }).then(res =>{
    console.log('完整响应数据：', res);
    // 修复判断逻辑：根据 msg 或 code 是否为 null 来判断（适配后端返回）
    if(res.msg === "请求成功" || res.data) {
      data.tableData = res.data?.list || [];
      data.total = res.data?.total || 0;
      ElMessage.success('查询成功，共 ' + data.total + ' 条数据');
    } else {
      ElMessage.error('查询失败：' + (res.msg || '未知错误'));
    }
  }).catch(err => {
    console.error("请求异常：", err);
    ElMessage.error('网络异常，请重试');
  });
}


const delBatch = () => {
  // 1. 校验选择的数据
  if (!data.ids || data.ids.length === 0) {
    ElMessage.warning("请选择需要删除的数据");
    return;
  }

  ElMessageBox.confirm(
      '批量删除后数据无法恢复，是否确认删除?',
      '批量删除确认',
      { type: 'warning' }
  ).then(async () => {
    try {
      // 2. 重点：POST请求，路径改为/category/batch/delete
      const res = await request.post("/category/batch/delete", data.ids);
      if (res.code === '200') {
        ElMessage.success('批量删除成功');
        load(); // 重新加载数据列表
      } else {
        ElMessage.error(res.msg || '批量删除失败');
      }
    } catch (err) {
      console.error('批量删除失败详情：', err);
      // 精准提示不同错误类型
      if (err.response?.status === 405) {
        ElMessage.error('接口请求方式错误，请检查后端接口注解');
      } else if (err.response?.status === 404) {
        ElMessage.error('批量删除接口不存在，请检查路径是否正确');
      } else {
        ElMessage.error(err.response?.data?.msg || '网络异常，批量删除失败');
      }
    }
  }).catch(err => {
    console.error('用户取消删除：', err);
  });
};

const handleSelectionChange = (row) =>{
  data.ids = row.map(v =>v.id)
  console.log(data.ids)
}

// ‘新增’按钮点击事件对应的函数
const handleAdd=()=>{
  data.form={}//数据存储在from中，form后续会联通Account类的属性,先清空表单
  data.formVisible=true //弹出对话框el-dialog，handle/操作，按钮点击时候设置为弹出对话框
}

//处理‘修改’按钮
// 编辑按钮点击事件：确保 scope.row 有值，且深拷贝
const handleEdit = (row) => {
  // 防御：如果 row 为空，直接提示
  if (!row || !row.id) {
    ElMessage.error('选中的数据无效，缺少 ID！');
    return;
  }
  // 深拷贝行数据到 form，确保 form 有 id
  data.form = JSON.parse(JSON.stringify(row));
  data.formVisible = true;
  console.log('编辑数据：', data.form); // 调试：确认 form 有 id
}

// 完善后的add函数（增加错误捕获+调试信息）
const add = () => {
  // 1. 打印请求信息，确认参数和路径
  console.log("请求路径：/category/add，请求参数：", data.form);

  request.post("/category/add", data.form)
      .then(res => {
        if(res.code === '200'){
          ElMessage.success('操作成功');
          data.formVisible = false;
          load(); // 重新加载列表
        } else {
          ElMessage.error(res.msg || '操作失败');
        }
      })
      // 2. 捕获网络错误（如404、500等）
      .catch(error => {
        console.error("请求失败详情：", error);
        // 分情况提示错误
        if (error.response) {
          // 服务器有响应但状态码错误（如404）
          ElMessage.error(`接口请求失败：${error.response.status}，请检查后端接口是否存在`);
        } else if (error.request) {
          // 无响应（后端服务未启动/端口错误）
          ElMessage.error("网络错误：无法连接到后端服务，请检查服务是否启动");
        } else {
          // 其他错误
          ElMessage.error("请求失败：" + error.message);
        }
      });
};

// 3. 更新函数：增加多层防御，避免 undefined
const update = async (formData) => {
  // 第一层防御：formData 本身是否存在
  if (!formData) {
    ElMessage.error('更新数据不能为空！');
    return;
  }
  // 第二层防御：是否有 id
  if (!formData.id || formData.id === '') {
    ElMessage.error('缺少更新主键 ID，无法修改！');
    return;
  }
  try {
    const res = await request.put('/category/update', formData);
    if (res.code === '200') {
      ElMessage.success('修改成功！');
      data.formVisible = false;
      load(); // 重新加载表格数据
    } else {
      ElMessage.error('修改失败：' + res.msg);
    }
  } catch (err) {
    console.error('更新接口请求失败：', err);
    ElMessage.error('网络异常，修改失败！');
  }
}



//确认操作的save对应的函数，函数还会结合request发送交互前后台的请求，然后函数save会调用request.js的接口
const save=()=>{
  data.form.id?update(data.form):add()
}

const del=(id)=>{
  ElMessageBox.confirm('数据删除后无法恢复，是否确认删除?','删除确认',{type:'warning' })
      .then(res=>{
        request.delete('/category/delete/'+id)
            .then(res=>{
              if(res.code === '200'){
                ElMessage.success("删除成功")
                load()
              }else{
                ElMessage.error(res.msg || '删除失败')
              }
            })
            // 补全请求异常的详细打印
            .catch(err=>{
              console.error('删除请求异常详情：', err)
              console.error('后端返回数据：', err.response?.data)
              ElMessage.error('删除操作发生系统异常：' + (err.response?.data?.msg || err.message))
            })
      }).catch(err=>{
    console.error('取消删除：', err)
  })
}

const reset=()=>{
  data.name=null;
  load()
}

load()
</script>

