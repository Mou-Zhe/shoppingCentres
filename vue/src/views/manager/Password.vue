<template>
  <div style="width: 40%;margin:20px auto" class="card">
    <el-form
        ref="formRef"
        :rules="data.rules"
        :model="data.user"
        label-width="90px"
        style="padding: 20px"
    >
      <el-form-item label="原密码" prop="password">
        <el-input v-model="data.word" placeholder="请输入原密码" show-password></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="data.user.newPassword" placeholder="请输入新密码" show-password></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="data.user.confirmPassword" placeholder="请确认新密码" show-password></el-input>
      </el-form-item>
      <div style="text-align: center">
        <el-button type="primary" @click="updatePassword">保 存</el-button>
      </div>
    </el-form>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import request from "@/utils/request.js";
import router from "@/router/index.js";

const formRef = ref();

// 修复：自定义校验函数（参数+逻辑错误修正）
const validatePass = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请确认密码')); // 未输入确认密码
  } else if (value !== data.user.newPassword) {
    callback(new Error("确认密码跟新密码不一致!")); // 密码不一致
  } else {
    callback(); // 校验通过，必须调用callback()
  }
};

const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  word:null,
  rules: {
    // 修复1：require → required（必填校验关键字错误）
    password: [{required: true, message: '请输入原密码', trigger: 'blur'}],
    newPassword: [{required: true, message: '请输入新密码', trigger: 'blur'}],
    confirmPassword: [
      {required: true, message: '请确认新密码', trigger: 'blur'}, // 补充必填校验
      {validator: validatePass, trigger: 'blur'} // 自定义一致性校验
    ]
  },
});

// 修复：完善更新密码逻辑（校验结果判断 + 异常捕获）
const updatePassword = () => {
  formRef.value.validate((valid) => {
    // 修复2：校验失败直接返回，不发送请求
    if (!valid) {
      ElMessage.warning('表单填写有误，请检查！');
      return;
    }

    // 修复3：添加try/catch捕获请求异常
    try {
      request.put('/updatePassword', data.user)
          .then(res => {
            if (res.code === '200') {
              ElMessage.success('密码修改成功，请重新登录');
              logout();
            } else {
              ElMessage.error(res.msg || '密码修改失败');
            }
          })
          .catch(error => {
            // 修复4：捕获网络/接口异常
            console.error('请求失败：', error);
            ElMessage.error(`请求异常：${error.message || '网络错误，请稍后重试'}`);
          });
    } catch (error) {
      ElMessage.error('系统异常，请联系管理员');
    }
  });
};

const logout = () => {
  localStorage.removeItem('xm-user');
  router.push('/login');
};
</script>

<style scoped>
.card {
  margin: 20px auto;
  background: #f5f7fa;
  border-radius: 8px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}
</style>