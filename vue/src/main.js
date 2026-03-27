import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import '@/assets/css/global.css'// 取消周边白色一圈边框，无边框化
import 'element-plus/dist/index.css'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'

const app=createApp(App)
app.use(ElementPlus, {locale: zhCn,})
app.use(router)
app.mount('#app')


import * as ElementPlusIconsVue from '@element-plus/icons-vue'

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}


