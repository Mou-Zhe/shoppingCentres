<template>
  <div style="width: 70%;margin: 20px auto;min-height: 100vh">
    <div class="cart" style="padding: 20px">
      <div style="margin-bottom: 15px">
        <el-input v-model="data.orderNo" prefix-icon="Search" style="width: 300px; height: 40px; margin-right: 10px" placeholder="请输入订单编号查询"></el-input>
        <el-button style=" height: 40px;" type="info" plain @click="load">查询</el-button>
        <el-button type="warning" plain style="margin: 0 10px; height: 40px;" @click="reset">重置</el-button>
      </div>

      <!--    表头-->
      <div style="margin-bottom: 15px">
        <el-table stripe :data="data.tableData" >
          <el-table-column type="expand">
            <template #default="props">

              <div style="padding: 10px">
                <h3>订单详情</h3>
                <el-table border :data="props.row.orderDetailList" stripe>
                  <el-table-column label="商品图片" prop="goodsImg">
                    <template #default="scope">
                      <el-image @click="router.push('/front/front_goodsDetail?id='+scope.row.goodsId)" style="width: 60px;height: 60px;border-radius: 5px" :src="scope.row.goodsImg"
                                :preview-src-list="[scope.row.goodsImg]" preview-teleported></el-image>
                    </template>
                  </el-table-column>
                  <el-table-column label="商品名称" prop="goodsName"></el-table-column>
                  <el-table-column label="商品单价" prop="goodsPrice">
                    <template #default="scope">
                      <b style="font-size: 20px;color: red">￥{{scope.row.price}}</b>
                    </template>
                  </el-table-column>
                  <el-table-column label="商品数量" prop="num">
                    <template #default="scope">
                      x{{scope.row.num}}
                    </template>
                  </el-table-column>
                  <el-table-column label="操作" width="100" fixed="right">
                    <template v-slot="scope">
                      <el-button size="small" type="primary" plain @click="handleComment(scope.row)" v-if="props.row.status==='已完成'">评价</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>


            </template>
          </el-table-column>
          <el-table-column prop="name" label="订单名称" show-overflow-tooltip />
          <el-table-column prop="orderNo" label="订单编号"show-overflow-tooltip />
          <el-table-column prop="total" label="总价格" >
            <template #default="scope">
              <b style="color:red">￥{{scope.row.total}}</b>
            </template>
          </el-table-column>
          <el-table-column prop="time" label="下单时间" width="180px" show-overflow-tooltip />
<!--          <el-table-column prop="userName" label="下单者" />-->
          <el-table-column  label="收货信息" show-overflow-tooltip>
            <template #default="scope">
<!--              收货者姓名没获取到-->
              {{scope.row}}
<!--              {{scope.row.addressName}}{{scope.row.addressPhone}}|{{scope.row.address}}-->
            </template>
          </el-table-column>
          <el-table-column label="订单状态">
            <template #default="scope">
              <el-tag type="danger" v-if="scope.row.status==='已取消'">已取消</el-tag>
              <el-tag type="warning" v-if="scope.row.status==='待支付'">待支付</el-tag>
              <el-tag type="primary" v-if="scope.row.status==='待发货'">待发货</el-tag>
              <el-tag type="info" v-if="scope.row.status==='待收货'">待收货</el-tag>
              <el-tag type="success" v-if="scope.row.status==='已完成'">已完成</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="140" fixed="right">
            <template v-slot="scope">
              <el-button size="small"  type="primary" plain @click="handlePay(scope.row.id)" v-if="scope.row.status==='待支付'">支付</el-button>
              <el-button size="small" type="danger" plain @click="cancel(scope.row)" v-if="scope.row.status==='待支付'" >取消</el-button>
              <el-button size="small" type="danger" plain @click="changeStatus(scope.row)" v-if="scope.row.status==='待收货'" >确认收货</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <div v-if="data.total">
      <el-pagination @current-change="load"  layout="total,prev,pager,next"  :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>

    <el-dialog title="支付" v-model="data.formVisible" width="30%" destroy-on-close>
      <div style="text-align: center; padding: 50px 0;">
        <el-radio-group v-model="data.payType">
          <el-radio value="zfb">
            <div style="display: flex; align-items: center;"><img src="@/assets/imgs/zfb.png" alt="" style="width: 50px; height: 50px">
              <span style="font-size: 20px; margin-left: 10px">支付宝</span>
            </div>
          </el-radio>
          <el-radio value="wx">
            <div style="display: flex; align-items: center;"><img src="@/assets/imgs/wx.png" alt="" style="width: 50px; height: 50px">
              <span style="font-size: 20px; margin-left: 10px">微信支付</span>
            </div>
          </el-radio>
        </el-radio-group>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="pay">确定支付</el-button>
        </span>
      </template>
    </el-dialog>
    <!--    输入表数据-->
    <el-dialog title="商品评价" v-model="data.formVisible1" width="40%" destroy-on-close>
      <el-form ref="form" :model="data.form1" label-width="70px" style="padding:20px">
        <el-form-item prop="content" label="评价内容">
          <el-input type="textarea" v-model="data.form1.content" placeholder="请输入评价内容"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span  class="dialog-footer">
         <el-button @click="data.formVisible1=false">取消</el-button>
         <el-button type="primary"  @click="saveComment">确定</el-button><!--确认后需要连接后端交互信息，在这之前需要进行数据效验-->
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import {reactive} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import {Delete} from "@element-plus/icons-vue";

const baseUrl=import.meta.env.VITE_BASE_URL

const handleFileUpload=(res) =>{
  data.form.avatar=res.data
}


const data=reactive({
  user:JSON.parse(localStorage.getItem('xm-user')||'{}'),
  formVisible:false, //弹出对话框el-daiglog，初始为不弹出
  formVisible1:false, //弹出对话框el-daiglog，初始为不弹出
  form:{},//数据存储在from中，form联通了Account类的属性,先清空表单
  form1:{},//数据存储在from中，form联通了Account类的属性,先清空表单
  tableData:[],
  pageNum:1,
  pageSize:5,
  total:0,
  orderNo:null,
  ids:[],
  orderId:null,
  // addressList:null,
  payType:'zfb',
})

// request.get('/address/selectAll',{
//   params:{
//     userId:data.user.id,
//   }
// }).then(res=>{
//   data.addressList=res.data
// })

const handleComment=(row)=>{
  data.formVisible1=true//打开弹窗
  data.form1={userId:data.user.id,goodsId:row.goodsId}
}

const saveComment=()=>{
  if(!data.form1.content){
    ElMessage.warning('请输入评价内容')
    return
  }
  request.post('/comment/add',data.form1).then(res => {
    if (res.code === '200') {
      ElMessage.success('评价成功')
      data.formVisible = false
    } else {
      ElMessage.error('评价失败：' + res.msg)
    }
  }).catch(err => {
    ElMessage.error("评价异常：网络或服务错误")
  })
}

// 3. 前台收货更新函数
const changeStatus = async (formData) => {
  ElMessageBox.confirm('商品收货后无法撤回，是否确认收货?','订单收货确认',{type:'warning' }).then(res=>{
    let form=JSON.parse(JSON.stringify(formData));
    form.status='已完成';
    request.put('/orders/update',form).then(res=>{
      if(res.code==='200'){
        ElMessage.success('收货成功')
        load()
      }else{
        ElMessage.error('收货失败：' + res.msg);
      }
    })
  }).catch()
}

const handlePay=(orderId)=>{
  data.orderId=orderId
  data.formVisible=true
}
const pay = () => {
  request.put('/orders/pay/' + data.orderId).then(res => {
    if (res.code === '200') {
      ElMessage.success('支付成功')
      data.formVisible = false
      load()
    } else {
      ElMessage.error('支付失败：' + res.msg)
    }
  }).catch(err => {
    ElMessage.error("支付异常：网络或服务错误")
  })
}

//查询表数据的接口
const load=()=>{
  request.get('/orders/selectPage',{
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      orderNo: data.orderNo,
      userId:data.user.id,
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
      // 2. POST请求，路径改为/orders/batch/delete
      const res = await request.post("/orders/batch/delete", data.ids);
      if (res.code === '200') {
        ElMessage.success('批量删除成功');
        load(); // 重新加载数据列表
      } else {
        ElMessage.error(res.msg || '批量删除失败');
      }
    } catch (err) {
      console.error('批量删除失败详情：', err);
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
  console.log("请求路径：/orders/add，请求参数：", data.form);

  request.post("/orders/add", data.form)
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
    const res = await request.put('/orders/update', formData);
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


// 3. 更新函数：增加多层防御，避免 undefined
const cancel = async (formData) => {
  ElMessageBox.confirm('订单取消后无法恢复，是否确认取消?','订单取消确认',{type:'warning' }).then(res=>{
    let form=JSON.parse(JSON.stringify(formData));
    form.status='已取消';
    request.put('/orders/update',form).then(res=>{
      if(res.code==='200'){
        ElMessage.success('取消订单成功')
        load()
      }else{
        ElMessage.error('修改失败：' + res.msg);
      }
    })
  }).catch()

}


//确认操作的save对应的函数，函数还会结合request发送交互前后台的请求，然后函数save会调用request.js的接口
const save=()=>{
  data.form.id?update(data.form):add()
}

const del=(id)=>{
  ElMessageBox.confirm('数据删除后无法恢复，是否确认删除?','删除确认',{type:'warning' })
      .then(res=>{
        request.delete('/orders/delete/'+id)
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
  data.orderNo=null;
  load()
}

load()
</script>

