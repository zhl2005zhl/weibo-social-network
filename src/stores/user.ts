import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login, register, getUserInfo } from '../services/auth'
import type { LoginRequest, RegisterRequest, UserInfo } from '../types/auth'

interface UserState {
  token: string | null
  userInfo: UserInfo | null
  isLoggedIn: boolean
}

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref<string | null>(localStorage.getItem('token'))
  const userInfo = ref<UserInfo | null>(null)
  
  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  
  // 动作
  const loginAction = async (data: LoginRequest) => {
    console.log('调用login函数，参数:', data)
    const response = await login(data)
    console.log('login函数返回:', response)
    token.value = response.token
    console.log('设置token:', response.token)
    localStorage.setItem('token', response.token)
    console.log('localStorage中的token:', localStorage.getItem('token'))
    await fetchUserInfo()
    return response
  }
  
  const registerAction = async (data: RegisterRequest) => {
    const response = await register(data)
    return response
  }
  
  const fetchUserInfo = async () => {
    console.log('开始获取用户信息')
    console.log('当前token:', token.value)
    console.log('localStorage中的token:', localStorage.getItem('token'))
    if (token.value) {
      try {
        const response = await getUserInfo()
        console.log('获取用户信息成功，响应:', response)
        userInfo.value = response
        return response
      } catch (error) {
        console.error('获取用户信息失败，错误:', error)
        throw error
      }
    }
    console.log('没有token，无法获取用户信息')
    return null
  }
  
  const logout = () => {
    token.value = null
    userInfo.value = null
    localStorage.removeItem('token')
  }
  
  return {
    token,
    userInfo,
    isLoggedIn,
    loginAction,
    registerAction,
    fetchUserInfo,
    logout
  }
})