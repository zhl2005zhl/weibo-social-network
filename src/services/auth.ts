import apiClient from './api'
import type { LoginRequest, RegisterRequest, LoginResponse, UserInfo } from '../types/auth'

/**
 * 用户登录
 */
export const login = (data: LoginRequest): Promise<{ token: string }> => {
  return apiClient.post('/v1/auth/login', data)
}

/**
 * 用户注册
 */
export const register = (data: RegisterRequest): Promise<void> => {
  return apiClient.post('/v1/auth/register', data)
}

/**
 * 获取当前用户信息
 */
export const getUserInfo = (): Promise<UserInfo> => {
  return apiClient.get('/v1/auth/me')
}

/**
 * 刷新Token
 */
export const refreshToken = (): Promise<{ token: string }> => {
  return apiClient.post('/v1/auth/refresh')
}