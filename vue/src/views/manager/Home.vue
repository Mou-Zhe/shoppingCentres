<template>
  <div>
    <div class="card" style="margin-bottom: 5px">您好！{{data.user?.name}},欢迎使用本后台系统  </div>
    <div class="card">
      <div style="height: 400px" id="line"></div>
    </div>
  </div>

</template>

<script setup>
import {onMounted, reactive} from "vue"
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import * as echarts from 'echarts'
const lineOption = {
  title: {
    text: '近30商品销售的趋势图',
    subtext: '趋势图',
    left: 'center'
  },
  tooltip: {
    trigger: 'axis'
  },
  legend: {
    left: 'left'
  },
  xAxis: {
    name: '日期',
    type: 'category',
    data: []
  },
  yAxis: {
    name: '销量',
    type: 'value'
  },
  grid: {
    top: '20%',
    bottom:'10%'
  },
  series: [
    {
      data: [],
      type: 'line',
      smooth: true,
      areaStyle: {
        opacity: 0.8, // 阴影的透明度
        color: 'rgb(185,190,255)' // 阴影的颜色和透明度
      },
      markPoint: {
        data: [
          { type: 'max', name: 'Max' },
          { type: 'min', name: 'Min' }
        ]
      },
      markLine: {
        data: [{ type: 'average', name: 'Avg' }]
      }
    },
  ]
}

// 等页面所有元素加载完成后再设置 echarts图表
onMounted(() => {
  // 请求数据  初始化图表
  // 折线图
  let lineDom = document.getElementById('line')
  let lineChart = echarts.init(lineDom)

  // 参考
  request.get('/selectLine').then(res => {
    lineOption.xAxis.data = res.data.date || []
    lineOption.series[0].data = res.data.count || []
    lineChart.setOption(lineOption)
  })
})

const data=reactive({
  user:JSON.parse(localStorage.getItem('xm-user')||'{}'),
})

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

