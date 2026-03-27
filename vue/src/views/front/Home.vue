<!--前台首页-->
<template>
  <div style="background-color: #e9f3f3;min-height: 100vh;padding-bottom: 100px">
    <div style="width: 70%;margin: 0 auto;">
      <el-carousel height="500px">
        <el-carousel-item v-for="item in data.banners" :key="item">
          <img :src="item.img" alt="" style="width: 100%; cursor: pointer" @click="router.push('/front/front_goodsDetail?id'+item.goodsId)">
        </el-carousel-item>
      </el-carousel>

      <div style="margin-bottom: 20px">
        <div style="color: red;font-size: 24px;font-weight: bold;margin-bottom: 20px">热销商品</div>
        <div>
          <el-row :gutter="20">
            <el-col :span="6" v-for="item in data.hotGoodsList" :key="item.id">
              <div style="cursor: pointer" @click="router.push('/front/front_goodsDetail?id='+item.id)" class="goods-item">
                <img :src="item.img" alt="" style="width: 100%;height: 260px;border-radius: 5px">
                <div style="font-size:18px;margin: 10px 0" class="line1">{{item.name}}</div>
                <div style="display:flex; align-items: center">
                  <div style="color:red;font-size: 20px;font-weight: bold;flex:1">￥{{item.price}}</div>
                  <div style="color: #666">{{item.saleCount}}人已购买</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>

      <div>
        <div style="display:flex;">
          <div style="color: red;font-size: 24px;font-weight: bold;flex:1;margin-bottom: 20px"  @click="router.push('/front/front_goods')">精选商品</div>
          <div style="cursor: pointer;color: red" @click="router.push('/front/goods')"> >>全部商品</div>
        </div>

<!--        -商品信息显示的布局-->
        <div>
          <el-row :gutter="20">
            <el-col :span="6" v-for="item in data.goodsList" :key="item.id">
              <div style="cursor: pointer;margin-bottom: 20px" @click="router.push('/front/front_goodsDetail?id='+item.id)" class="goods-item">
                <img :src="item.img" alt="" style="width: 100%;height: 260px;border-radius: 5px">
                <div style="font-size:18px;margin: 10px 0" class="line1">{{item.name}}</div>
                <div style="display:flex; align-items: center">
                  <div style="color:red;font-size: 20px;font-weight: bold;flex:1">￥{{item.price}}</div>
                  <div style="color: #666">{{item.saleCount}}人已购买</div>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </div>


    </div>
  </div>
</template>

<script setup>

import {reactive} from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";


const  data=reactive({
  banners:[],
  hotGoodsList:[],
  goodsList:[]
})

request.get('/banner/selectAll').then(res=>{
  data.banners=res.data
})


request.get('/goods/selectHot').then(res=>{
  data.hotGoodsList=res.data
})

request.get('/goods/selectPage',  {
  params:{
    pageNum:1,
    pageSize:8}
}).then(res=>{
  data.goodsList=res.data.list
})
</script>

<style>

.goods-item img{
  transition: all 0.2s;
}
.goods-item:hover img{
  scale: 1.05;
}
</style>

