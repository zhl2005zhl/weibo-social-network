import apiClient from './api'
import type { Follow } from '../types/follow'

/**
 * 关注用户
 */
export const followUser = (followingId: number): Promise<void> => {
  return apiClient.post(`/v1/follows/following/${followingId}`)
}

/**
 * 取消关注用户
 */
export const unfollowUser = (followingId: number): Promise<void> => {
  return apiClient.delete(`/v1/follows/following/${followingId}`)
}

/**
 * 检查是否已关注用户
 */
export const isFollowing = (followerId: number, followingId: number): Promise<boolean> => {
  return apiClient.get(`/v1/follows/${followerId}/following/${followingId}`)
}

/**
 * 获取用户的关注列表
 */
export const getFollowingList = (userId: number): Promise<Follow[]> => {
  return apiClient.get(`/v1/follows/${userId}/following`)
}

/**
 * 获取用户的粉丝列表
 */
export const getFollowersList = (userId: number): Promise<Follow[]> => {
  return apiClient.get(`/v1/follows/${userId}/followers`)
}

/**
 * 获取用户的关注数
 */
export const getFollowingCount = (userId: number): Promise<number> => {
  return apiClient.get(`/v1/follows/${userId}/following-count`)
}

/**
 * 获取用户的粉丝数
 */
export const getFollowersCount = (userId: number): Promise<number> => {
  return apiClient.get(`/v1/follows/${userId}/followers-count`)
}
