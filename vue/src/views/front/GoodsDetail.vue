<template>
  <div style="background-color: #f7f7ff;min-height: 100vh;padding-bottom: 100px">
    <div style="width: 60%;margin: 0 auto;padding: 20px 0">
      <div class="card" style="padding: 20px;display: flex;grid-gap: 20px">
        <!-- 修复1：用:src绑定响应式数据，而非静态src -->
        <img :src="data.goods.img "  alt="" style="width: 350px;height: 350px">
        <div style="flex: 1">
          <div style="font-weight: bold;font-size: 20px">{{data.goods.name}}</div>
          <div style="background-color: red;padding-left: 10px; height: 30px; line-height:30px;color: white;font-weight: bold">顾客精选，超高品质，欢迎您的选购</div>
          <div style="margin: 20px 0;color: #666">{{data.goods.description}}</div>
          <div style="margin: 10px 0;color: #666">价格:<b style="=font-size:20px;color:red">￥{{data.goods.price}} </b>{{data.goods.unit}}</div>
          <div style="margin: 10px 0;color: #666">库存:<span style="color: #333">{{data.goods.store}}</span></div>
          <div style="margin: 10px 0;color: #666">销量:<span style="color: #333">{{data.goods.saleCount}}</span></div>
          <div style="margin-bottom: 50px; color: #666">提示：商品支持7天无理由退款，如果存在质量问题，请联系售后客服沟通</div>
          <div style="display: flex;align-items: center">
            <el-input-number style="height: 40px;width: 100px" v-model="data.num" :min="1"></el-input-number>
            <el-button @click="handleBuy" style="height: 40px;width: 100px;margin-left: 10px" type="danger">立即购买</el-button>
            <el-button @click="addCart" style="height: 40px;width: 100px;margin-left: 10px" type="danger">加入购物车</el-button>
            <div style="margin-left: 10px;text-align: center; cursor: pointer">
              <div @click="handleCollect">
                <el-icon size="18" v-if="data.goods.userCollect"> <StarFilled color="red"/></el-icon>
                <el-icon size="18" v-else> <Star/></el-icon>
              </div>
              <div @click="handleCollect" style="margin-top:-5px">
                <span style="color: red;font-size: 12px"  v-if="data.goods.userCollect">取消收藏</span>
                <span style="color: #666;font-size: 12px" v-else>收藏</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="card" style="padding: 20px;margin: 20px 0">
        <div style="font-size: 20px;font-weight: bold;margin-bottom: 40px">用户评价({{data.total}})</div><!--div更换h2，不需要h2的默认边框-->
        <div>
          <div v-for="item in data.commentList" :key="item.id">
            <div style="display: flex; grid-gap: 20px;margin-bottom: 15px">
              <img style="width: 50px;height: 50px;border-radius: 50%" :src="item.userAvatar" alt="">
              <div style="flex:1;border-bottom: 1px solid #ddd;padding-bottom: 15px">
                <div style="margin-bottom: 5px">{{item.userName}}</div>
                <div style="margin-bottom: 5px;color: #888;font-size: 12px">{{item.time}}·{{item.goodsName}}</div>
                <div >{{item.content}}</div>
              </div>
            </div>
          </div>
        </div>
        <div v-if="data.total">
          <el-pagination @current-change="load"  layout="total,prev,pager,next"  :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
        </div>
      </div>
    </div>
    <el-dialog title="立即购买" v-model="data.formVisible" width="40%" destroy-on-close>
      <div style="padding: 30px;margin-bottom: 50px">
        <div style="margin-bottom: 15px"><b>购买：{{data.goods.name}}x{{data.num}}</b></div>
        <div style="display: flex;align-items: center;">
          <div style="width: 100px">收货地址：</div>
          <el-select style="width: 600px" v-model="data.addressId">
            <el-option v-for="item in data.addressList" :key="item.id" :value="item.id"
                       :label="item.name+'|'+item.phone+'|'+item.address" ></el-option>
          </el-select>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="buy">确定支付</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";
import {ElMessage} from "element-plus";

const data=reactive({//获取当前路径附带的id
  user:JSON.parse(localStorage.getItem('xm-user') || '{}'),
  id:router.currentRoute.value.query.id,
  goods:{},
  num:1,
  formVisible:false,
  addressId:null,
  addressList:[],
  total:0,
  pageNum:1,
  pageSize:10,
  commentList:[],
})

//查询表数据的接口
const loadComment=()=>{
  request.get('/comment/selectPage',{
    params:{
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      goodsId: data.id
    }
  }).then(res =>{
    console.log('完整响应数据：', res);
    // 修复判断逻辑：根据 msg 或 code 是否为 null 来判断（适配后端返回）
    if(res.msg === "请求成功" || res.data) {
      data.commentList = res.data?.list || [];
      data.total = res.data?.total || 0;
      ElMessage.success('查询成功，共 ' + data.total + ' 条评价数据');
    } else {
      ElMessage.error('查询失败：' + (res.msg || '未知错误'));
    }
  }).catch(err => {
    console.error("请求异常：", err);
    ElMessage.error('网络异常，请重试');
  });
}
loadComment()

const buy=()=>{
  if(!data.addressId){
    ElMessage.warning('请选择收货地址')
    return
  }
  request.post("/orders/add?addressId="+data.addressId,[{goodsId:data.id,userId:data.user.id,num:data.num}]).then(res=>{
    if(res.code==='200'){
      ElMessage.success('下单成功！')
      //跳转到订单页面
      router.push('/front/front_orders')
    }else {
      ElMessage.error(res.msg)
    }
  })
}

request.get('/address/selectAll',{
  params:{userId:data.user.id}
}).then(res=>{
  data.addressList=res.data
})

const handleBuy=()=>{
  data.formVisible=true
  data.addressId=null
}

const  addCart=()=>{
  request.post('/cart/add',{goodsId:data.id,userId:data.user.id,num:data.num}).then(res=>{
    if(res.code==='200'){
      ElMessage.success('加入购物车成功')
      //重新加载商品信息
      load()
    }else{
      ElMessage.error('加入购物车失败')
    }
  })
}


//收藏/取消收藏
const handleCollect=()=>{
  request.post('collect/add',{userId:data.user.id,goodsId: data.id}).then(res=>{
    if(res.code==='200'){
      ElMessage.success('收藏成功')
          //重新加载商品信息
      load()
    }else{
      ElMessage.error('收藏失败')
    }
  })
}

const load=()=>
{
  request.get('/goods/selectById/' + data.id).then(res => {
    data.goods = res.data//根据data的id查询到商品的信息
  })
}
load()
</script>

<style>

</style>