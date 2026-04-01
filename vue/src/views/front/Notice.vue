<template>
  <div style="width: 50%;margin: 0 auto;padding: 20px 0;min-height: 70vh">
    <div class="card" style="padding: 20px">
      <div style="margin-bottom: 50px;font-size: 20px;font-weight: bold">系统公告</div>
      <div>
        <el-timeline style="max-width: 600px">
          <el-timeline-item
              color="#0bbd87"
              v-for="(item, index) in data.noticeDate"
              :key="index"
              :timestamp="item.time"
              placement="top">
<!--     placement="top"     将时间戳放文字上面-->
            <div style="font-weight: bold;font-size:16px;margin-bottom: 10px">{{item.title}}</div>
           <div> {{ item.content }}</div>
          </el-timeline-item>
        </el-timeline>
      </div>
    </div>
  </div>

</template>

<script setup>
import {reactive} from"vue"
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";

const data=reactive({
  user:JSON.parse(localStorage.getItem('xm-user')||'{}'),
  noticeDate:[]
})

const loadNotice=()=>{
  request.get('/notice/selectAll').then(res=>{
    if(res.code==='200'){
      data.noticeDate=res.data
    }else{
      ElMessage.error(res.msg)
    }
  })
}

loadNotice()
</script>

<style>

</style>
<!--<template>-->
<!--  <div>-->
<!--    <div class="card">您好！欢迎使用本后台系统</div>-->
<!--    <div>-->
<!--      &lt;!&ndash; 可选：添加加载状态/结果展示 &ndash;&gt;-->
<!--      <el-button-->
<!--          type="primary"-->
<!--          @click="fetchHelloData"-->
<!--          :loading="loading"-->
<!--      >-->
<!--        测试请求接口-->
<!--      </el-button>-->
<!--      <div v-if="responseData" style="margin-top: 20px;">-->
<!--        接口返回结果：{{ responseData }}-->
<!--      </div>-->
<!--    </div>-->
<!--  </div>-->
<!--</template>-->

<!--<script setup>-->
<!--import { ref, onMounted } from "vue";-->
<!--import request from "@/utils/request.js";-->
<!--import { ElMessage } from "element-plus";-->

<!--// 1. 定义状态变量（规范管理）-->
<!--const loading = ref(false); // 请求加载状态-->
<!--const responseData = ref(null); // 存储接口返回数据-->

<!--// 2. 封装请求方法（复用+可控制）-->
<!--const fetchHelloData = async () => {-->
<!--  try {-->
<!--    loading.value = true; // 开始加载-->
<!--    // 发送GET请求（支持传参：params: { id: 123 }）-->
<!--    const res = await request.get('test/hello', {-->
<!--      // params: { key: 'value' } // 如有请求参数，添加这里-->
<!--    });-->

<!--    // 接口响应处理-->
<!--    if (res.code === '200' || res.code === 200) {-->
<!--      responseData.value = res.data; // 存储返回数据-->
<!--      console.log('接口请求成功：', res);-->
<!--      ElMessage.success('请求成功！');-->
<!--    } else {-->
<!--      ElMessage.warning(`请求返回异常：${res.msg || '未知错误'}`);-->
<!--    }-->
<!--  } catch (error) {-->
<!--    // 捕获网络错误/接口报错-->
<!--    console.error('接口请求失败：', error);-->
<!--    ElMessage.error(`请求失败：${error.message || '网络异常，请稍后重试'}`);-->
<!--  } finally {-->
<!--    loading.value = false; // 结束加载（无论成功/失败）-->
<!--  }-->
<!--};-->

<!--// 3. 页面加载时自动请求（可选，也可通过按钮触发）-->
<!--onMounted(() => {-->
<!--  // 页面挂载后自动执行请求（注释掉则只通过按钮触发）-->
<!--  // fetchHelloData();-->
<!--});-->
<!--</script>-->

<!--<style scoped>-->
<!--.card {-->
<!--  padding: 20px;-->
<!--  background-color: #f5f7fa;-->
<!--  border-radius: 8px;-->
<!--  margin-bottom: 20px;-->
<!--  font-size: 16px;-->
<!--  color: #333;-->
<!--}-->
<!--</style>-->

