<template>
  <div style="background-color: #f7f7ff; min-height: 100vh; padding-bottom: 100px">
    <div style="width: 70%; margin: 0 auto; padding: 20px 0">
      <div class="card" style="padding: 20px">

        <div style="margin-bottom: 20px">
          <el-input @clear="loadGoods" clearable v-model="data.name" placeholder="请输入商品名称搜索" style="width: 400px; height: 40px; margin-right: 10px"></el-input>
          <el-button @click="loadGoods" type="primary" plain style="height: 40px">搜 索</el-button>
        </div>

        <div style="display: flex; align-items: center; margin-bottom: 20px">
          <div @click="selectCategory(null)" style="margin-right: 20px; cursor: pointer; padding-bottom: 5px" :class="{ 'category-active' : data.currentCategoryId === null }">全部</div>
          <div @click="selectCategory(item.id)" :class="{ 'category-active' : data.currentCategoryId === item.id }" style="margin-right: 20px; cursor: pointer; padding-bottom: 5px" v-for="item in data.categoryList" :key="item.id">{{ item.name }}</div>
        </div>

        <div>
          <el-row :gutter="20" style="margin-bottom: 20px">
            <el-col :span="6" v-for="item in data.goodsList" :key="item.id">
              <div style="cursor: pointer; margin-bottom: 20px" @click="router.push('/front/front_goodsDetail?id=' + item.id)"  class="goods-item">
                <img :src="item.img" alt="" style="width: 100%; height: 260px; border-radius: 5px">
                <div style="font-size: 18px; margin: 10px 0" class="line1">{{ item.name }}</div>
                <div style="display: flex; align-items: center">
                  <div style="color: red; font-size: 20px; font-weight: bold; flex: 1">￥{{ item.price }}</div>
                </div>
              </div>
            </el-col>
          </el-row>

          <div v-if="data.total">
            <el-pagination @current-change="loadGoods"  layout="total, prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total" />
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request.js";
import router from "@/router/index.js";

const data = reactive({
  name: null,
  categoryList: [],
  currentCategoryId: null,
  pageNum: 1,
  pageSize: 8,
  total: 0,
  goodsList: []
})




const clearPathParam = () => {
  let url = location.href
  url = url.replace(/(\?|#)[^'"]*/, '');           //去除参数
  window.history.pushState({},0, url);
}

//拿到当前URL中的关键字，用于页面交互跳转
let urlName = router.currentRoute.value.query.name
if (urlName) {
  data.name = urlName
  clearPathParam()
}


request.get('/category/selectAll').then(res => {
  data.categoryList = res.data
})

const selectCategory = (categoryId) => {
  data.currentCategoryId = categoryId
  loadGoods()
}

const loadGoods = () => {
  request.get('/goods/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      name: data.name,
      categoryId: data.currentCategoryId
    }
  }).then(res => {
    data.goodsList = res.data.list
    data.total = res.data.total
  })
}
loadGoods()
</script>

<style>
.category-active {
  color: red;
  border-bottom: 2px solid red;
}

.goods-item img{
  transition: all 0.5s;
}
.goods-item:hover img{
  scale: 1.05;
}
</style>