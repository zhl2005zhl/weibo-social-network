// 登录请求类型
export interface LoginRequest {
  username: string
  password: string
}

// 注册请求类型
export interface RegisterRequest {
  username: string
  password: string
  email: string
  nickname: string
}

// 用户信息类型
export interface UserInfo {
  id: number
  username: string
  nickname: string
  email: string
  phone?: string
  avatarId?: number
  bio?: string
  location?: string
  website?: string
  createdAt: string
  updatedAt: string
  status: number
}

// 登录响应类型
export interface LoginResponse {
  token: string
  userInfo: UserInfo
}