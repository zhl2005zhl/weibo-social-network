// 通知类型
export interface Notification {
  id: number
  userId: number
  senderId: number
  type: number
  content: string
  targetId: number
  isRead: number
  createdAt: string
  sender?: {
    id: number
    username: string
    nickname: string
    avatarId?: number
  }
  typeName?: string
}

// 通知类型枚举
export enum NotificationType {
  LIKE = 1,
  COMMENT = 2,
  REPOST = 3,
  FOLLOW = 4
}

// 通知类型名称映射
export const NotificationTypeNameMap = {
  [NotificationType.LIKE]: '点赞',
  [NotificationType.COMMENT]: '评论',
  [NotificationType.REPOST]: '转发',
  [NotificationType.FOLLOW]: '关注'
}
