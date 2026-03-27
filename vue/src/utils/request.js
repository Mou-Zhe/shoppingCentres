import axios from "axios";
import { ElMessage } from "element-plus";
import router from "@/router/index.js";
// 若需要路由跳转（比如401重定向登录），需引入router（根据你的项目路径调整）
// import router from "@/router";

const request = axios.create({
    baseURL: import.meta.env.VITE_BASE_URL, // 修复：Localhost 改为 localhost（大小写不敏感，但规范小写）
    timeout: 30000 // 后台请求超时时间
});

// 请求拦截器：请求发送前的处理
request.interceptors.request.use(
    (config) => {
        // 修复1：header 改为 headers（axios 配置的正确属性名，原代码少个s）
        config.headers['Content-Type'] = 'application/json;charset=utf-8';

        //前端的axios请求携带缓存的token
        let user= JSON.parse(localStorage.getItem("xm-user")||'{}')
        config.headers['token']=user.token||''
        // 可选：添加token等通用请求头（示例）
        // const token = localStorage.getItem('token');
        // if (token) {
        //   config.headers['Authorization'] = `Bearer ${token}`;
        // }

        return config;
    },
    (error) => {
        // 捕获请求拦截器错误，友好提示
        ElMessage.error('请求配置异常，请稍后重试');
        return Promise.reject(error);
    }
);

// 响应拦截器：接口响应后统一处理
request.interceptors.response.use(
    (response) => {
        let res = response.data;

        // 如果是返回文件（如导出Excel），直接返回blob对象
        if (response.config.responseType === 'blob') {
            return res;
        }
// 权限不通过时候给出提示
        if (res && res.code === '401') {
            ElMessage.error(res.msg || '登录已过期，请重新登录');
            localStorage.clear();
            router.push('/login')
        }

        // 兼容服务端返回的字符串数据（防止JSON.parse报错）
        if (typeof res === 'string') {
            try {
                res = JSON.parse(res);
            } catch (e) {
                // 解析失败时不强制转换，避免报错中断流程
                console.warn('响应数据解析为JSON失败，原始数据：', res);
            }
        }

        // 可选：401权限验证（需先引入router）
        // if (res.code === '401') {
        //   ElMessage.warning('登录状态失效，请重新登录');
        //   router.push("/login");
        // }

        return res;
    },
    (error) => {
        // 核心修复：先校验 error.response 是否存在，避免读取undefined的status
        if (!error.response) {
            // 无响应的场景：网络错误、超时、跨域等
            if (error.message.includes('timeout')) {
                ElMessage.error('请求超时，请检查网络或稍后重试');
            } else if (error.message.includes('Network')) {
                ElMessage.error('网络异常，请检查网络连接');
            } else {
                ElMessage.error(`请求失败：${error.message || '未知错误'}`);
            }
        } else {
            // 有响应但状态码非2xx的场景
            const status = error.response.status;
            switch (status) {
                case 404:
                    ElMessage.error('请求接口不存在（404）');
                    break;
                case 500:
                    ElMessage.error('服务器内部错误（500），请查看后端控制台');
                    break;
                case 401:
                    ElMessage.warning('登录状态失效，请重新登录');
                    // router.push("/login"); // 需引入router后启用
                    break;
                case 403:
                    ElMessage.error('没有权限访问该接口（403）');
                    break;
                default:
                    ElMessage.error(`请求失败，状态码：${status}`);
            }
        }

        // 打印详细错误信息，便于调试
        console.error('请求错误详情：', error);
        return Promise.reject(error);
    }
);

// 导出供前端使用
export default request;