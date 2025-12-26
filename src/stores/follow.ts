import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import * as followApi from '../services/follow'
import type { Follow, UserStats } from '../types/follow'

interface FollowState {
  followingList: Follow[]
  followersList: Follow[]
  userStats: Map<number, UserStats>
  loading: boolean
  error: string | null
}

export const useFollowStore = defineStore('follow', () => {
  // 状态
  const followingList = ref<Follow[]>([])
  const followersList = ref<Follow[]>([])
  const userStats = ref<Map<number, UserStats>>(new Map())
  const loading = ref(false)
  const error = ref<string | null>(null)
  
  // 计算属性
  const totalFollowing = computed(() => followingList.value.length)
  const totalFollowers = computed(() => followersList.value.length)
  
  // 动作
  const fetchFollowingList = async (userId: number) => {
    loading.value = true
    error.value = null
    try {
      const data = await followApi.getFollowingList(userId)
      followingList.value = data
      return data
    } catch (err: any) {
      error.value = err.message
      return []
    } finally {
      loading.value = false
    }
  }
  
  const fetchFollowersList = async (userId: number) => {
    loading.value = true
    error.value = null
    try {
      const data = await followApi.getFollowersList(userId)
      followersList.value = data
      return data
    } catch (err: any) {
      error.value = err.message
      return []
    } finally {
      loading.value = false
    }
  }
  
  const toggleFollow = async (userId: number, isFollowing: boolean) => {
    loading.value = true
    error.value = null
    try {
      if (isFollowing) {
        await followApi.unfollowUser(userId)
        // 更新关注列表
        followingList.value = followingList.value.filter(follow => follow.followingId !== userId)
        // 更新用户统计信息
        updateUserStats(userId, { followingCount: -1 })
      } else {
        await followApi.followUser(userId)
        // 这里简化处理，实际应该重新获取关注列表或添加到列表中
        // 更新用户统计信息
        updateUserStats(userId, { followersCount: 1 })
      }
    } catch (err: any) {
      error.value = err.message
      throw err
    } finally {
      loading.value = false
    }
  }
  
  const checkFollowStatus = async (followerId: number, followingId: number) => {
    return await followApi.isFollowing(followerId, followingId)
  }
  
  const fetchUserStats = async (userId: number) => {
    loading.value = true
    error.value = null
    try {
      const [followingCount, followersCount] = await Promise.all([
        followApi.getFollowingCount(userId),
        followApi.getFollowersCount(userId)
      ])
      const stats: UserStats = {
        followingCount,
        followersCount,
        postsCount: 0 // 这里需要从动态服务获取
      }
      userStats.value.set(userId, stats)
      return stats
    } catch (err: any) {
      error.value = err.message
      return null
    } finally {
      loading.value = false
    }
  }
  
  const updateUserStats = (userId: number, changes: Partial<UserStats>) => {
    const stats = userStats.value.get(userId) || {
      followingCount: 0,
      followersCount: 0,
      postsCount: 0
    }
    userStats.value.set(userId, {
      ...stats,
      followingCount: stats.followingCount + (changes.followingCount || 0),
      followersCount: stats.followersCount + (changes.followersCount || 0),
      postsCount: stats.postsCount + (changes.postsCount || 0)
    })
  }
  
  const getUserStats = (userId: number) => {
    return userStats.value.get(userId) || {
      followingCount: 0,
      followersCount: 0,
      postsCount: 0
    }
  }
  
  return {
    followingList,
    followersList,
    userStats,
    loading,
    error,
    totalFollowing,
    totalFollowers,
    fetchFollowingList,
    fetchFollowersList,
    toggleFollow,
    checkFollowStatus,
    fetchUserStats,
    updateUserStats,
    getUserStats
  }
})