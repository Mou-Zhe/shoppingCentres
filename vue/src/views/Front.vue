<!--前台首页-->
<template>
  <div>
<!--    <div class="front-notice"><el-icon><Bell/></el-icon> 公告:{{data.top}}</div>-->
    <div class="front-header">
      <div class="front-header-left">
        <img src="@/assets/imgs/logo.png" alt="">
        <div class="title" @click="router.replace('/front/home')">购物商城</div>
      </div>

      <div class="front-header-center">
        <el-menu :default-active="router.currentRoute.value.path" router mode="horizontal" style="background: #e9f2ff;flex:1">
          <el-menu-item index="/front/home">首页</el-menu-item>
          <el-menu-item index="/front/front_goods">精选商品</el-menu-item>
          <el-menu-item index="/front/front_cart">购物车</el-menu-item>
          <el-menu-item index="/front/front_orders">我的订单</el-menu-item>
          <el-menu-item index="/front/front_notice">系统公告</el-menu-item>
        </el-menu>
        <div style="width: 350px;margin-right: 50px" v-if="router.currentRoute.value.path!=='/front/front_goods'">
          <el-input clearable style="width: 200px;margin-right: 5px;height: 40px" v-model="data.name" placeholder="请输入商品名称进行搜索"></el-input><!--宽度设置200px以免超限-->
          <el-button type="primary" style="height: 40px" @click="router.push('/front/front_goods?name='+data.name)">搜 索</el-button>
        </div>
      </div>

      <div class="front-header-right">
        <div  v-if="!data.user.id">
          <el-button @click="router.push('/login')">登录</el-button>
          <el-button @click="router.push('/register')">注册</el-button>
        </div>
        <div v-else></div>


        <el-dropdown style="cursor: pointer">
          <div style="display: flex;align-items: center">
            <img style=" width:40px;height: 40px;border-radius:50%;display: block":src="data.user.avatar" alt="">
            <span style="margin-left: 5px;">{{data.user.name}}</span><el-icon><arrow-down/></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/front/front_person')">个人信息</el-dropdown-item>
              <el-dropdown-item @click="router.push('/front/front_userCollect')">我的收藏</el-dropdown-item>
              <el-dropdown-item @click="router.push('/front/front_userComment')">我的评价</el-dropdown-item>
              <el-dropdown-item @click="router.push('/front/front_address')">我的收货地址</el-dropdown-item>
              <el-dropdown-item @click="router.push('/front/front_password')">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <div class="main-body">
      <RouterView @updateUser="updateUser"/>
    </div>
    <Footer/>
  </div>

</template>


<script setup>
import router from "@/router/index.js";
import {reactive} from "vue";
import Footer from "@/components/Footer.vue";


const data=reactive({
   user:JSON.parse(localStorage.getItem('xm-user'||'{}')),//获取缓存中的数据或者赋值为空
  // top:null,
  // noticeDate:[],
  name:null
})/*reactive是vue内置的变量，通过其可以实现数据的绑定*/
const logout =()=>{
  localStorage.removeItem('xm-user')//去除缓存
  router.push('/login')
}

const front_person=()=>{
  router.push('/front/front_person')
}

const updateUser=()=>{
  data.user=JSON.parse(localStorage.getItem('xm-user')||'{}')
}
// const loadNotice=()=>{
//   request.get('/notice/selectAll').then(res=>{
//     data.noticeDate=res.data
//     let i=0
//     if(data.noticeDate&&data.noticeDate.length){
//       data.top=data.noticeDate[0].content
//       setInterval(()=>{
//         data.top=data.noticeDate[i].content
//         i++
//         if(i===data.noticeDate.length){
//           i=0
//         }
//       },2500)
//     }
//   })
// }
//
// loadNotice()页脚在9
</script>

<style scoped>
@import "@/assets/css/front.css";
</style>
<!--在vue中导入对应的css,<script setup>是让脚本编写更方便的样式，<script scoped>是让样式不打架的隔离罩，是两个vue3中提升开发体验和组件封装性的两个独立重要的特性，通常一起使用-->