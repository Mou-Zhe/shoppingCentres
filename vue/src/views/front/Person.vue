<template>
  <div style="width: 40%; margin:20px  auto" class="card">
    <el-form ref="user" :model="data.user" label-width="60px" style="padding:20px">
      <div style="text-align: right">
        <el-button type="primary" @click="handleAccount">充值</el-button>
      </div>
       <div style="text-align: center; margin-bottom:20px"> <el-upload
            :action="baseUrl+'/files/upload'"
            :on-success="handleFileUpload"
            class="avatar-uploader"
        >
          <img v-if="data.user.avatar" :src="data.user.avatar" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
       </div>


      <el-form-item prop="username" label="用户名">
        <el-input :disabled="true" v-model="data.user.username" placeholder="请输入用户名"></el-input>
      </el-form-item>

      <el-form-item prop="name" label="姓名">
        <el-input v-model="data.user.name" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item prop="phone" label="电话">
        <el-input v-model="data.user.phone" placeholder="请输入电话号码"></el-input>
      </el-form-item>
      <el-form-item prop="email" label="邮箱"> <!-- 修复：prop值和label匹配 -->
        <el-input v-model="data.user.email" placeholder="请输入电子邮箱"></el-input>
      </el-form-item>
      <el-form-item prop="email" label="余额"> <!-- 修复：prop值和label匹配 -->
        <div>￥{{data.user.account}}</div>
      </el-form-item>
      <div style="text-align: center">
        <el-button style="height: 40px;width: 100px" type="primary" @click="update">保 存</el-button>
      </div>
    </el-form>


<!--    充值弹窗-->
    <el-dialog title="充值" v-model="data.formVisible" width="30%"   destroy-on-close>
      <el-form ref="form" :model="data.form" label-width="70px" style="padding:20px">
        <el-form-item prop="account" label="余额">
          <el-input-number style="width:200px" :min="0"  v-model="data.form.account" placeholder="请输入充值金额"></el-input-number>
        </el-form-item>
      </el-form>
      <template #footer>
        <span  class="dialog-footer">
         <el-button @click="data.formVisible=false">取消</el-button>
         <el-button type="primary"  @click="updateAccount">确定</el-button><!--确认后需要连接后端交互信息，在这之前需要进行数据效验-->
        </span>
      </template>
    </el-dialog>


  </div>
</template>

<script setup>
import { reactive } from "vue";
import { ElMessage } from "element-plus";
import request from "@/utils/request.js";

// 初始化用户数据
const data = reactive({
  user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
  formVisible:false,
  form:{ account:0}
})


const handleAccount=()=>{
  data.formVisible=true
}

const updateAccount=()=>{
  request.put('/user/updateAccount/'+data.form.account).then(res=>{
    if(res.code==='200'){
      ElMessage.success('充值成功')
      data.formVisible=false//关闭弹窗
      loadUser()
    }else{
      ElMessage.error(res.msg)
    }
  })
}


const loadUser=()=>{
  request.get('/user/selectById/'+data.user.id).then(res=>{
    if(res.code==='200'){//只需要更新account而不是清除token
      data.user.account=res.data.account
      localStorage.setItem('xm-user', JSON.stringify(data.user));
    }else{
      ElMessage.error(res.msg)
    }
  })
}

// 基础URL（确保.env文件中配置了VITE_BASE_URL）
const baseUrl = import.meta.env.VITE_BASE_URL || 'http://localhost:9090';

// 头像上传成功回调
const handleFileUpload = (res) => {
  data.user.avatar = res.data;
}

//更新修改后的页面 事件
const emit=defineEmits(['updateUser'])

// 保存更新方法（完整修复版）
const update = async () => {
  try {
    // 1. 基础数据校验
    if (!data.user.id) { // 假设用户有id字段，无id则不提交
      ElMessage.warning('用户信息不完整，请重新登录');
      return;
    }

    // 2. 打印调试信息（方便排查问题）
    console.log('当前用户数据：', data.user);
    console.log('用户角色：', data.user.role);
    console.log('请求基础URL：', baseUrl);

    // 3. 兼容不同的角色值（比如大小写、小写等）
    const userRole = (data.user.role || '').toUpperCase();

    // 4. 根据角色提交请求（扩展：可添加其他角色处理）
    let apiUrl = '';
    if (userRole === 'USER') { // 如果有普通用户
      apiUrl = '/user/update';
    } else {
      // 无匹配角色时的处理
      ElMessage.warning(`暂不支持${data.user.role}角色的信息修改`);
      return;
    }

    // 5. 发送更新请求（添加完整的错误捕获）
    const res = await request.put(apiUrl, data.user);

    // 6. 处理响应结果
    if (res.code === '200' || res.code === 200) { // 兼容字符串/数字类型的code
      ElMessage.success('保存成功');
      // 更新本地存储
      localStorage.setItem('xm-user', JSON.stringify(data.user));
      emit('updateUser')//发送给父类
    } else {
      ElMessage.error(`保存失败：${res.msg || '未知错误'}`);
    }

  } catch (error) {
    // 7. 捕获所有异常（网络错误、接口错误等）
    console.error('保存请求异常：', error);
    ElMessage.error(`保存失败：${error.message || '网络异常，请稍后重试'}`);
  }
}
</script>

<style>
.avatar-uploader{
  height: 120px;
}
.avatar-uploader .avatar {
  width: 120px;
  height: 120px;
  display: block;
}

.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  text-align: center;
  line-height: 120px; /* 修复：图标垂直居中 */
}
</style>
<!--<template>-->
<!--<div style="width: 50%" class="card">-->
<!--  个人资料-->
<!--  <el-form ref="user" :model="data.user" label-width="70px" style="padding:20px">-->

<!--    <el-form-item prop="avatar" label="头像">-->
<!--      <el-upload-->
<!--          :action="baseUrl+'/files/upload'"-->
<!--          :on-success="handleFileUpload"-->
<!--          class="avatar-uploader"-->
<!--      >-->
<!--        <img v-if="data.user.avatar" :src="data.user.avatar" class="avatar" />-->
<!--        <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>-->
<!--      </el-upload>-->
<!--    </el-form-item>-->
<!--    <el-form-item prop="username" label="用户名">-->
<!--      <el-input :disabled="true" v-model="data.user.username" placeholder="请输入用户名"></el-input>-->
<!--    </el-form-item>-->

<!--    <el-form-item prop="name" label="姓名">-->
<!--      <el-input v-model="data.user.name" placeholder="请输入姓名"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item prop="phone" label="电话">-->
<!--      <el-input v-model="data.user.phone" placeholder="请输入电话号码"></el-input>-->
<!--    </el-form-item>-->
<!--    <el-form-item prop="username" label="电子邮箱">-->
<!--      <el-input v-model="data.user.email" placeholder="请输入电子邮箱"></el-input>-->
<!--    </el-form-item>-->
<!--    <div style="text-align: center">-->
<!--      <el-button type="primary" @click="update">保 存</el-button>-->
<!--    </div>-->
<!--  </el-form>-->
<!--</div>-->
<!--</template>-->

<!--<script setup>-->
<!--import {reactive} from "vue";-->
<!--import {ElMessage} from "element-plus";-->
<!--import request from "@/utils/request.js";-->
<!--const data =reactive({-->
<!--  user:JSON.parse(localStorage.getItem('xm-user')||'{}')-->
<!--})-->

<!--const baseUrl = import.meta.env.VITE_BASE_URL-->
<!--const handleFileUpload = (res)=>{-->
<!--  data.user.avatar=res.data-->
<!--}-->

<!--const update=()=>{-->
<!--  if(data.user.role==='ADMIN'){-->
<!--    request.put('/admin/update',data.user).then(res=>{-->
<!--      if(res.code==='200'){-->
<!--        ElMessage.success('保存成功')-->
<!--        localStorage.setItem('xm-user',JSON.stringify(data.user))-->
<!--      }else{-->
<!--        ElMessage.error(res.msg)-->
<!--      }-->
<!--    })-->
<!--  }-->
<!--  //ElMessage.success('cg')-->
<!--}-->
<!--</script >-->


<!--<style>-->
<!--.avatar-uploader{-->
<!--  height: 120px;-->
<!--}-->
<!--.avatar-uploader .avatar {-->
<!--  width: 120px;-->
<!--  height: 120px;-->
<!--  display: block;-->
<!--}-->

<!--.avatar-uploader .el-upload {-->
<!--  border: 1px dashed var(&#45;&#45;el-border-color);-->
<!--  border-radius: 6px;-->
<!--  cursor: pointer;-->
<!--  position: relative;-->
<!--  overflow: hidden;-->
<!--  transition: var(&#45;&#45;el-transition-duration-fast);-->
<!--}-->

<!--.avatar-uploader .el-upload:hover {-->
<!--  border-color: var(&#45;&#45;el-color-primary);-->
<!--}-->

<!--.el-icon.avatar-uploader-icon {-->
<!--  font-size: 28px;-->
<!--  color: #8c939d;-->
<!--  width: 120px;-->
<!--  height: 120px;-->
<!--  text-align: center;-->
<!--}-->
<!--</style>-->