<template>
  <div style="width: 70%;margin: 20px auto;min-height: 100vh">
    <h2>我收藏的商品</h2>
    <!--        -商品信息显示的布局-->
    <div class="card"  style="padding: 20px" v-if="data.collectList?.length">
      <div style="margin-bottom: 20px;text-align: right" ><span @click="clearCollect" style="cursor: pointer;color: #666">一键清空</span></div>
      <el-row :gutter="20">
        <el-col :span="6" v-for="item in data.collectList" :key="item.id">
          <div style="cursor: pointer;margin-bottom: 20px" @click="router.push('/front/front_goodsDetail?id='+item.goodsId)" class="goods-item">
            <img :src="item.goodsImg" alt="" style="width: 100%;height: 260px;border-radius: 5px">
            <div style="font-size:18px;margin: 10px 0" class="line1">{{item.goodsName}}</div>
            <div style="display:flex; align-items: center">
              <div style="color:red;font-size: 20px;font-weight: bold;flex:1">￥{{item.goodsPrice}}</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="card" style="padding:50px;text-align: center;color: #666" v-else>暂无收藏......</div>
  </div>
</template>

<script setup>
import {reactive} from "vue";
import router from "@/router/index.js";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
const data=reactive({
  user:JSON.parse(localStorage.getItem('xm-user') || '{}'),
  collectList:[],
})

//一键清空收藏列标
const clearCollect=()=>{
  ElMessageBox.confirm('清除收藏后无法恢复，您确定全部清除吗？','清除确认',{type:'warning'}).then(res=>{
    let ids=data.collectList.map(v =>v.id)//ES6中map的新语法v=v.id而不是（）=>id
    request.delete('/collect/batch/delete',{data:ids}).then(res=>{
      if(res.code==='200'){
        ElMessage.success('操作成功')
        load()
      }else{
        ElMessage.error(res.msg)
      }
    })
  })
}

const load=()=>{
  request.get('/collect/selectAll',{
    params:{
      userId:data.user.id
    }
  }).then(res=>{

    data.collectList=res.data
  })
}

load()
</script>


<style>

</style>