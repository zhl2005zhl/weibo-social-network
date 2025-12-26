import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import * as notificationApi from '../services/notification'
import type { Notification } from '../types/notification'
import { NotificationTypeNameMap } from '../types/notification'

interface NotificationState {
  notifications: Notification[]
  unreadCount: number
  loading: boolean
  error: string | null
}

export const useNotificationStore = defineStore('notification', () => {
  // 状态
  const notifications = ref<Notification[]>([])
  const unreadCount = ref(0)
  const loading = ref(false)
  const error = ref<string | null>(null)
  
  // 计算属性
  const hasNotifications = computed(() => notifications.value.length > 0)
  const hasUnreadNotifications = computed(() => unreadCount.value > 0)
  
  // 动作
  const fetchNotifications = async () => {
    loading.value = true
    error.value = null
    try {
      const data = await notificationApi.getNotifications()
      // 为每个通知添加类型名称
      const notificationsWithTypeName = data.map(notification => ({
        ...notification,
        typeName: NotificationTypeNameMap[notification.type as keyof typeof NotificationTypeNameMap]
      }))
      notifications.value = notificationsWithTypeName
      return notificationsWithTypeName
    } catch (err: any) {
      error.value = err.message
      return []
    } finally {
      loading.value = false
    }
  }
  
  const fetchUnreadNotifications = async () => {
    loading.value = true
    error.value = null
    try {
      const data = await notificationApi.getUnreadNotifications()
      // 为每个通知添加类型名称
      const notificationsWithTypeName = data.map(notification => ({
        ...notification,
        typeName: NotificationTypeNameMap[notification.type as keyof typeof NotificationTypeNameMap]
      }))
      return notificationsWithTypeName
    } catch (err: any) {
      error.value = err.message
      return []
    } finally {
      loading.value = false
    }
  }
  
  const fetchUnreadCount = async () => {
    loading.value = true
    error.value = null
    try {
      const count = await notificationApi.getUnreadCount()
      unreadCount.value = count
      return count
    } catch (err: any) {
      error.value = err.message
      return 0
    } finally {
      loading.value = false
    }
  }
  
  const markNotificationAsRead = async (id: number) => {
    loading.value = true
    error.value = null
    try {
      await notificationApi.markAsRead(id)
      // 更新本地状态
      const notification = notifications.value.find(n => n.id === id)
      if (notification) {
        notification.isRead = 1
      }
      if (unreadCount.value > 0) {
        unreadCount.value--
      }
      return true
    } catch (err: any) {
      error.value = err.message
      return false
    } finally {
      loading.value = false
    }
  }
  
  const markAllNotificationsAsRead = async () => {
    loading.value = true
    error.value = null
    try {
      await notificationApi.markAllAsRead()
      // 更新本地状态
      notifications.value.forEach(notification => {
        notification.isRead = 1
      })
      unreadCount.value = 0
      return true
    } catch (err: any) {
      error.value = err.message
      return false
    } finally {
      loading.value = false
    }
  }
  
  return {
    notifications,
    unreadCount,
    loading,
    error,
    hasNotifications,
    hasUnreadNotifications,
    fetchNotifications,
    fetchUnreadNotifications,
    fetchUnreadCount,
    markNotificationAsRead,
    markAllNotificationsAsRead
  }
})