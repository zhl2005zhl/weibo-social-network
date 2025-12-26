<template>
  <div class="notification-container">
    <el-container>
      <el-main>
        <div class="notification-header">
          <h1>通知</h1>
          <el-button
            v-if="unreadCount > 0"
            type="primary"
            size="small"
            :loading="loading"
            @click="handleMarkAllAsRead"
            class="mark-all-read-btn"
          >
            全部已读
          </el-button>
        </div>
        
        <el-tabs v-model="activeTab" class="notification-tabs">
          <el-tab-pane label="全部" name="all">
            <div class="notification-list">
              <div
                v-for="notification in notifications"
                :key="notification.id"
                class="notification-item"
                :class="{ 'unread': notification.isRead === 0 }"
                @click="handleNotificationClick(notification)"
              >
                <div class="notification-avatar">
                  <el-avatar :size="50">
                    {{ notification.sender?.nickname?.charAt(0) || 'U' }}
                  </el-avatar>
                </div>
                <div class="notification-content">
                  <div class="notification-title">
                    <span class="sender-name">{{ notification.sender?.nickname }}</span>
                    <span class="notification-type">{{ notification.typeName }}</span>
                    <span class="notification-text">{{ notification.content }}</span>
                  </div>
                  <div class="notification-time">{{ formatTime(notification.createdAt) }}</div>
                </div>
                <div class="notification-actions">
                  <el-button
                    v-if="notification.isRead === 0"
                    type="text"
                    size="small"
                    @click.stop="handleMarkAsRead(notification.id)"
                  >
                    标记已读
                  </el-button>
                </div>
              </div>
              <el-empty v-if="!hasNotifications" description="暂无通知" />
            </div>
          </el-tab-pane>
          <el-tab-pane label="未读" name="unread">
            <div class="notification-list">
              <div
                v-for="notification in unreadNotifications"
                :key="notification.id"
                class="notification-item"
                :class="{ 'unread': notification.isRead === 0 }"
                @click="handleNotificationClick(notification)"
              >
                <div class="notification-avatar">
                  <el-avatar :size="50">
                    {{ notification.sender?.nickname?.charAt(0) || 'U' }}
                  </el-avatar>
                </div>
                <div class="notification-content">
                  <div class="notification-title">
                    <span class="sender-name">{{ notification.sender?.nickname }}</span>
                    <span class="notification-type">{{ notification.typeName }}</span>
                    <span class="notification-text">{{ notification.content }}</span>
                  </div>
                  <div class="notification-time">{{ formatTime(notification.createdAt) }}</div>
                </div>
                <div class="notification-actions">
                  <el-button
                    type="text"
                    size="small"
                    @click.stop="handleMarkAsRead(notification.id)"
                  >
                    标记已读
                  </el-button>
                </div>
              </div>
              <el-empty v-if="unreadNotifications.length === 0" description="暂无未读通知" />
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useNotificationStore } from '../stores/notification'
import type { Notification } from '../types/notification'

const router = useRouter()
const notificationStore = useNotificationStore()

const notifications = computed(() => notificationStore.notifications)
const unreadNotifications = computed(() => notificationStore.notifications.filter(n => n.isRead === 0))
const unreadCount = computed(() => notificationStore.unreadCount)
const hasNotifications = computed(() => notificationStore.hasNotifications)
const loading = ref(false)
const activeTab = ref('all')

const formatTime = (time: string): string => {
  const now = new Date()
  const notificationTime = new Date(time)
  const diff = now.getTime() - notificationTime.getTime()
  
  const minutes = Math.floor(diff / 60000)
  const hours = Math.floor(diff / 3600000)
  const days = Math.floor(diff / 86400000)
  
  if (minutes < 60) {
    return `${minutes}分钟前`
  } else if (hours < 24) {
    return `${hours}小时前`
  } else if (days < 30) {
    return `${days}天前`
  } else {
    return notificationTime.toLocaleDateString()
  }
}

const handleMarkAsRead = async (id: number) => {
  try {
    const success = await notificationStore.markNotificationAsRead(id)
    if (success) {
      ElMessage.success('标记已读成功')
    }
  } catch (error: any) {
    ElMessage.error(error.message || '操作失败')
  }
}

const handleMarkAllAsRead = async () => {
  try {
    const success = await notificationStore.markAllNotificationsAsRead()
    if (success) {
      ElMessage.success('全部标记已读成功')
    }
  } catch (error: any) {
    ElMessage.error(error.message || '操作失败')
  }
}

const handleNotificationClick = (notification: Notification) => {
  // 如果是未读通知，点击后标记为已读
  if (notification.isRead === 0) {
    notificationStore.markNotificationAsRead(notification.id)
  }
  
  // 根据通知类型跳转到相应页面
  switch (notification.type) {
    case 1: // 点赞
    case 2: // 评论
    case 3: // 转发
      // 跳转到动态详情页
      router.push(`/post/${notification.targetId}`)
      break
    case 4: // 关注
      // 跳转到用户主页
      router.push(`/user/${notification.targetId}`)
      break
    default:
      break
  }
}

onMounted(async () => {
  await notificationStore.fetchNotifications()
  await notificationStore.fetchUnreadCount()
})
</script>

<style scoped>
.notification-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.notification-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.notification-tabs {
  margin-top: 20px;
}

.notification-list {
  margin-top: 20px;
}

.notification-item {
  display: flex;
  align-items: flex-start;
  padding: 15px;
  margin-bottom: 15px;
  border-radius: 8px;
  background-color: #f9f9f9;
  cursor: pointer;
  transition: all 0.3s ease;
}

.notification-item:hover {
  background-color: #f0f0f0;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.notification-item.unread {
  background-color: #e6f7ff;
  border-left: 4px solid #1890ff;
}

.notification-avatar {
  margin-right: 15px;
}

.notification-content {
  flex: 1;
}

.notification-title {
  font-size: 16px;
  line-height: 1.5;
  margin-bottom: 5px;
}

.sender-name {
  font-weight: bold;
  color: #333;
}

.notification-type {
  color: #1890ff;
  margin: 0 5px;
}

.notification-text {
  color: #666;
}

.notification-time {
  font-size: 12px;
  color: #999;
}

.notification-actions {
  margin-left: 15px;
}

.mark-all-read-btn {
  font-size: 14px;
}
</style>