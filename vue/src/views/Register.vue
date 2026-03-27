<template>
  <div class="login-container">
    <div style="padding: 20px;font-size: 30px;color: #1450aa;font-weight: bold;position: absolute;top: 0;left: 0">刘谋麟的第三套个人管理系统</div>
    <div class="login-box">
      <div  style="font-weight: bold;font-size: 24px;text-align: center;margin-bottom: 30px;color: #1450aa">欢迎注册</div>
     <el-form ref="formRef" :model="data.form" :rules="data.rules">
       <el-form-item prop="username">
         <el-input :prefixo-icon="User" size="large" v-model="data.form.username" placeholder="请输入账号"></el-input>
       </el-form-item>
       <el-form-item prop="password">
         <el-input show-password :prefixo-icon="Lock" size="large" v-model="data.form.password" placeholder="请输入密码"></el-input>
       </el-form-item>
       <el-form-item prop="confirmPassword">
         <el-input show-password :prefixo-icon="Lock" size="large" v-model="data.form.confirmPassword" placeholder="请确认密码"></el-input>
       </el-form-item>
       <el-form-item>
         <el-button size="large" type="primary" style="width: 100%;background-color: #656ABA;border-color: #656ABA" @click="register">注册</el-button>
       </el-form-item>
       <div style="text-align: right">
         已有账号？请<a style="color: #A6C8FF" href="/login">登录</a>
       </div>
     </el-form>
    </div>
  </div>
</template>

<script setup>
import {User} from"@element-plus/icons-vue"
import {reactive,ref} from "vue";
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";

const validatePass = (rule, value, callback) => {//element-plus的效验方法
  if (!value) {
    callback(new Error('请确认密码'))
  } else {
    if (value!==data.form.password) {
      callback(new Error("确认密码与原密码不一致！"))
    }
    callback()
  }
}

const data=reactive({
  form:{},
  rules:{
    username:[
      {required:true,message:'请输入账号',trigger:'blur'}
    ],
    password:[
      {required:true,message:'请确认密码',trigger:'blur'}
    ],
    confirmPassword:[{validator: validatePass, trigger: 'blur' }]
  }
})



const Lock = ref(false)

const  register=()=>{
  formRef.value.validate(valid =>{
    if(valid){//表示表单效验通过
      request.post('/register',data.form).then(res =>{
        if(res.code === '200'){
          ElMessage.success('注册成功')
          router.push('/login')
        }else{
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const formRef=ref()

</script>



<style scoped>

.login-container {
  height: 100vh;
  overflow: hidden;
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 修正拼写错误：垂直居中 */
  background: linear-gradient(to top, #A6C8FF, #A6C8FF);
  box-sizing: border-box; /* 防止padding/border影响尺寸 */
}

.login-box {
  width: 350px;
  padding: 30px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 0.5); /* 提高透明度，视觉更清爽 */
  box-sizing: border-box; /* 让padding包含在宽度内，避免超出350px */
}
</style>
<!--<template>-->
<!--  <div class="login-container">-->
<!--    <div class="login-box">-->

<!--    </div>-->
<!--  </div>-->
<!--</template>-->


<!--<script setup>-->

<!--</script>-->

<!--<style scoped>-->
<!--.login-container{-->
<!--  height: 100vh;-->
<!--  overflow: hidden;-->
<!--  display: flex;-->
<!--  justify-content: center;-->
<!--  align-items: center;-->
<!--  background:linear-gradient(to top,#7f7fd5,#86a8e7,#91eae4);-->
<!--}-->

<!--.login-box{-->
<!--  width: 350px;-->
<!--  padding: 30px;-->
<!--  border-radius: 5px;-->
<!--  box-shadow: 0 0 10px rgba(0,0 ,0,0.1);-->
<!--  background-color: rgba(255,255,255,0.5);-->
<!--}-->
<!--</style>-->