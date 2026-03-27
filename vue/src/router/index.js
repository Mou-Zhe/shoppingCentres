// 路由导向总文件
import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path:'/',redirect:'/login'},
    {
      path:'/manager',
      component:()=>import('@/views/Manger.vue'),//通过/manager这条路由到import导入的页面,这个路由不区分大小写
      children:[
          {path:'home',meta:{ name: '系统首页' },component:()=>import('@/views/manager/Home.vue'),},
          {path: 'admin',meta:{name:'管理员信息页面'} , component: () => import('@/views/manager/Admin.vue'),},
          {path: 'user',meta:{name:'普通用户信息页面'} , component: () => import('@/views/manager/User.vue'),},
          {path: 'person',meta:{name:'个人资料'} , component: () => import('@/views/manager/Person.vue'),},
          {path: 'notice',meta:{name:'系统公告'} , component: () => import('@/views/manager/Notice.vue'),},
          {path: 'password',meta:{name:'修改密码'} , component: () => import('@/views/manager/Password.vue'),},
          {path: 'category',meta:{name:'商品分类'} , component: () => import('@/views/manager/Category.vue'),},
          {path: 'banner',meta:{name:'商品轮播图'} , component: () => import('@/views/manager/Banner.vue'),},
          {path: 'collect',meta:{name:'商品收藏信息'} , component: () => import('@/views/manager/Collect.vue'),},
          {path: 'goods',meta:{name:'商品信息'} , component: () => import('@/views/manager/Goods.vue'),},
          {path: 'orders',meta:{name:'收货信息'} , component: () => import('@/views/manager/Orders.vue'),}
      ]/*子路由，不用/*/
      /*以下四句是转向另一个vue页面
      path:'/',
      name:'home',
       component:HomeView
      component:()=>import('../views/AboutView.vue')*/
    },
    {
      path: '/front',
      component: () => import('@/views/Front.vue'),//通过/views这条路由到import导入的页面
      children: [
        {path: 'Home',component: () => import('@/views/front/Home.vue'),},
        {path: 'front_person',component: () => import('@/views/front/Person.vue'),},
        {path: 'front_password',component: () => import('@/views/front/Password.vue'),},
        {path: 'front_goodsDetail',component: () => import('@/views/front/GoodsDetail.vue'),},
        {path: 'front_userCollect',component: () => import('@/views/front/UserCollect.vue'),},
        {path: 'front_address',component: () => import('@/views/front/Address.vue'),},
        {path: 'front_cart',component: () => import('@/views/front/Cart.vue'),},
        {path: 'front_goods',component: () => import('@/views/front/Goods.vue'),},
        {path: 'front_orders',component: () => import('@/views/front/Orders.vue'),},

      ]/*子路由，不用/*/
    },
    {path:'/login',component:() =>import('@/views/Login.vue')},
    {path:'/Register',component:() =>import('@/views/Register.vue')},
    { path:'/404',component:()=>import('@/views/404.vue')},
    {path:'/:pathMatch(.*)',redirect:'/404'}/*所有找不到路由的路径都跳转到404页面*/
  ]
})


// 页面自动滚动到页面顶部
router.beforeEach(()=>{
    window.scrollTo({top:0,behavior:"smooth"})
})

export default router
