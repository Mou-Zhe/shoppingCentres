<template>
  <div class="login-container">
    <div style="padding: 20px;font-size: 30px;color: #656ABA;font-weight: bold;position: absolute;top: 0;left: 0">刘谋麟的第三套个人管理系统</div>
    <div class="login-box">
      <div style="font-weight: bold;font-size: 24px;text-align: center;margin-bottom: 30px;color: #1450aa">欢迎登录</div>
      <el-form ref="formRef" :model="data.form" :rules="data.rules">
        <el-form-item prop="username">
          <!-- 修复1：拼写错误 prefixo-icon → prefix-icon -->
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入账号"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <!-- 修复2：导入Lock图标并修正拼写 -->
          <el-input show-password :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item prop="role">
          <el-select size="large" v-model="data.form.role">
            <el-option value="ADMIN" label="管理员"></el-option>
            <el-option value="USER" label="用户"></el-option>
            <el-option value="VIP" label="VIP用户"></el-option>
            <el-option value="S-VIP" label="S-VIP用户"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" style="width: 100%;background-color: #656ABA;border-color: #656ABA" @click="login">登录</el-button>
        </el-form-item>
        <div style="text-align: right">
          还没有账号？请<a style="color: #656ABA" href="/register">注册</a>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
// 修复3：导入缺失的Lock图标
import { User, Lock } from "@element-plus/icons-vue";
import { reactive, ref } from "vue";
import request from "@/utils/request.js";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";

const formRef = ref()

const data = reactive({
  form: {
    username: '',
    password: '',
    role: 'USER'
  },
  rules: {
    username: [
      { required: true, message: '请输入账号', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' }
    ],
    role: [
      { required: true, message: '请选择角色', trigger: 'change' }
    ]
  }
})

// 移除无用的lock变量（原代码中未使用）
// const lock = ref(false)

const login = () => {
  formRef.value.validate(valid => {
    if (valid) { // 表单校验通过
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          res.data.role =data.form.role;
          if(res.data?.id) {//用户在后端存在ID,前端缓存才存储信息到浏览器，以防止null缓存占据位置导致正确输入也会登录失败
            localStorage.setItem('xm-user',JSON.stringify(res.data))
            if(res.data.role === 'USER') {
              router.push({ path: '/front/home' });
            }else{
              router.push('/manager/home');
            }
            // 调试日志（验证role是否存入）
            console.log('用户选择的role：', data.form.role);
            console.log('最终存储的用户信息：', res.data);//这里存储的信息还是正确的
            console.log('缓存中的xm-user：', localStorage.getItem('xm-user'));//这里存储的信息还是正确的

            ElMessage.success('登录成功');
          }
        } else {
          // 修复5：添加接口返回非200的提示
          ElMessage.error(res.msg || '登录失败，请重试');
        }
      }).catch(error => {
        // 修复6：捕获请求异常（网络错误、接口报错）
        console.error('登录请求失败：', error);
        ElMessage.error('登录失败：' + (error.message || '网络异常'));
      })
    }
  })
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(to top, #adadf4, #edf2fa);
  box-sizing: border-box;
}

.login-box {
  width: 350px;
  padding: 30px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  background-color: rgba(255, 255, 255, 0.5);
  box-sizing: border-box;
}
</style>


<!--<template>-->
<!--  <div class="login-container">-->
<!--    <div class="login-box">-->
<!--      <div  style="font-weight: bold;font-size: 24px;text-align: center;margin-bottom: 30px;color: #1450aa">欢迎登录</div>-->
<!--     <el-form ref="formRef" :model="data.form" :rules="data.rules">-->
<!--       <el-form-item prop="username">-->
<!--         <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入账号"></el-input>-->
<!--       </el-form-item>-->
<!--       <el-form-item prop="password">-->
<!--         <el-input show-password :prefixo-icon="lock" size="large" v-model="data.form.password" placeholder="请输入密码"></el-input>-->
<!--       </el-form-item>-->
<!--       <el-form-item prop="role">-->
<!--         <el-select size="large" v-model="data.form.role">-->
<!--           <el-option value="ADMIN" label="管理员"></el-option>-->
<!--           <el-option value="USER" label="用户"></el-option>-->
<!--           <el-option value="VIP" label="VIP用户"></el-option>-->
<!--           <el-option value="S-VIP" label="S-VIP用户"></el-option>-->
<!--         </el-select>-->
<!--       </el-form-item>-->
<!--       <el-form-item>-->
<!--         <el-button size="large" type="primary" style="width: 100%" @click="login">登录</el-button>-->
<!--       </el-form-item>-->
<!--       <div style="text-align: right">-->
<!--         还没有账号？请<a href="/register">注册</a>-->
<!--       </div>-->
<!--     </el-form>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->

<!--<script setup>-->
<!--import {User} from"@element-plus/icons-vue"-->
<!--import {reactive,ref} from "vue";-->
<!--import request from "@/utils/request.js";-->
<!--import {ElMessage} from "element-plus";-->
<!--import router from "@/router/index.js";-->

<!--const formRef=ref()-->

<!--const data=reactive({-->
<!--  form:{-->
<!--    username:'',-->
<!--    password:'',-->
<!--    role:''},-->
<!--  rules:{-->
<!--    username:[-->
<!--      {required:true,message:'请输入账号',trigger:'blur'}-->
<!--    ],-->
<!--    password:[-->
<!--      {required:true,message:'请输入密码',trigger:'blur'}-->
<!--    ],-->
<!--    role: [-->
<!--      { required: true, message: '请选择角色', trigger: 'change' }-->
<!--    ]-->

<!--  }-->
<!--})-->
<!--const lock = ref(false)-->
<!--const  login=()=>{-->
<!--  formRef.value.validate(valid =>{-->
<!--    if(valid){//表示表单效验通过-->
<!--      request.post('/login',data.form).then(res =>{-->
<!--        if(res.code === '200'){-->
<!--          ElMessage.success('登录成功')-->
<!--          //存储用户信息到浏览器的缓存-->
<!--          localStorage.setItem('xm-user',JSON.stringify(res.data))-->
<!--          router.push('/manager/home')-->
<!--          // 登录接口请求成功后的回调里-->
<!--          console.log('接口返回完整数据：', res); // 看 res 是否有 data 字段-->
<!--          console.log('要存储的 data：', res.data); // 看 res.data 是否是预期值-->

<!--          if (res && res.data) { // 加判空，避免 undefined 存储-->
<!--            localStorage.setItem('xm-user', JSON.stringify(res.data));-->
<!--            console.log('存储成功，localStorage 内容：', localStorage.getItem('xm-user')); // 验证存储结果-->
<!--           }else {-->
<!--            console.error('res.data 不存在，无法存储：', res);-->
<!--          }-->
<!--        }-->
<!--      })-->
<!--    }-->
<!--  })-->
<!--}-->

<!--</script>-->

<!--<style scoped>-->

<!--.login-container {-->
<!--  height: 100vh;-->
<!--  overflow: hidden;-->
<!--  display: flex;-->
<!--  justify-content: center; /* 水平居中 */-->
<!--  align-items: center; /* 修正拼写错误：垂直居中 */-->
<!--  background: linear-gradient(to top, #7f7fd5, #86a8e7, #91eae4);-->
<!--  box-sizing: border-box; /* 防止padding/border影响尺寸 */-->
<!--}-->

<!--.login-box {-->
<!--  width: 350px;-->
<!--  padding: 30px;-->
<!--  border-radius: 5px;-->
<!--  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);-->
<!--  background-color: rgba(255, 255, 255, 0.8); /* 提高透明度，视觉更清爽 */-->
<!--  box-sizing: border-box; /* 让padding包含在宽度内，避免超出350px */-->
<!--}-->
<!--</style>-->




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