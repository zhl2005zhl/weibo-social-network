import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'

// 创建axios实例
const apiClient = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
apiClient.interceptors.request.use(
  (config) => {
    // 直接从localStorage获取token，避免在非组件环境中使用useUserStore()
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
      console.log('请求头中添加了Authorization:', `Bearer ${token.substring(0, 20)}...`)
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
apiClient.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    // 针对不同接口返回不同的数据结构
    // 登录和刷新token返回整个响应对象，因为token在根级
    if (response.config.url?.includes('/login') || response.config.url?.includes('/refresh')) {
      return res
    }
    // 其他接口返回res.data
    return res.data
  },
  (error) => {
    // 处理401错误，显示登录过期并跳转到登录页
    if (error.response?.status === 401) {
      const userStore = useUserStore()
      userStore.logout()
      ElMessage.error('登录已过期，请重新登录')
      window.location.href = '/login'
    } else {
      ElMessage.error(error.message || '网络错误')
    }
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

export default apiClient