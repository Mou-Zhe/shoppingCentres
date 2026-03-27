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
            <el-button style="height: 40px;width: 100px;margin-left: 10px" type="danger">立即购买</el-button>
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
      <div>
      </div>
    </div>
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
  num:1
})

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