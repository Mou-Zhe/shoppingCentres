<template>
  <div>

    <div class="card" style="margin-bottom: 5px">
      <el-input v-model="data.goodsName" prefix-icon="Search" style="width: 240px; margin-right: 10px" placeholder="请输入商品名称查询"></el-input>
      <el-button type="info" plain @click="load">查询</el-button>
      <el-button type="warning" plain style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 5px">
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

<!--    表头-->
    <div class="card" style="margin-bottom: 5px">
      <el-table stripe :data="data.tableData" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="goodsImg" label="商品图片" >
          <template #default="scope">
            <el-image style="width: 50px; height: 50px; display: block; border-radius: 5px" :src="scope.row.goodsImg" :preview-src-list="[scope.row.goodsImg]" preview-teleported></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="goodsName" label="商品名称" />
        <el-table-column prop="userName" label="用户名称" />
        <el-table-column prop="time" label="收藏时间" />
        <el-table-column label="操作" width="100" fixed="right">
          <template v-slot="scope">
            <el-button type="danger" circle :icon="Delete" @click="del(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card" v-if="data.total">
      <el-pagination @current-change="load" background layout="prev,pager,next"  :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>
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
  goodsName:null,
  ids:[]
})

//查询表数据的接口
const load=()=>{
  request.get('/collect/selectPage',{
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      goodsName: data.goodsName,
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
      // 2. 重点：POST请求，路径改为/collect/batch/delete
      const res = await request.post("/collect/batch/delete", data.ids);
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


const del=(id)=>{
  ElMessageBox.confirm('数据删除后无法恢复，是否确认删除?','删除确认',{type:'warning' })
      .then(res=>{
        request.delete('/collect/delete/'+id)
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
  data.title=null;
  load()
}

load()
</script>

