import apiClient from './api'
import type { Notification } from '../types/notification'

/**
 * 获取通知列表
 */
export const getNotifications = (): Promise<Notification[]> => {
  return apiClient.get('/v1/notifications')
}

/**
 * 获取未读通知列表
 */
export const getUnreadNotifications = (): Promise<Notification[]> => {
  return apiClient.get('/v1/notifications/unread')
}

/**
 * 将通知标记为已读
 */
export const markAsRead = (id: number): Promise<void> => {
  return apiClient.put(`/v1/notifications/${id}/read`)
}

/**
 * 将所有通知标记为已读
 */
export const markAllAsRead = (): Promise<void> => {
  return apiClient.put('/v1/notifications/read-all')
}

/**
 * 获取未读通知数量
 */
export const getUnreadCount = (): Promise<number> => {
  return apiClient.get('/v1/notifications/unread-count')
}
