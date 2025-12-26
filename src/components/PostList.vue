<template>
  <div class="post-list">
    <div v-for="post in posts" :key="post.id" class="post-card">
      <div class="post-header">
        <el-avatar :src="post.author?.avatarId ? `http://localhost:8080/api/files/${post.author.avatarId}` : ''" size="large">
          {{ post.author?.nickname?.charAt(0) || 'U' }}
        </el-avatar>
        <div class="author-info">
          <div class="author-name">{{ post.author?.nickname }}</div>
          <div class="post-time">{{ formatTime(post.createdAt) }}</div>
        </div>
      </div>
      <div class="post-content">{{ post.content }}</div>
      <div class="post-files" v-if="post.files && post.files.length > 0">
        <div class="file-item" v-for="file in post.files" :key="file.id">
          <el-image :src="file.url" fit="cover" class="post-image" preview-teleported />
        </div>
      </div>
      <div class="post-actions">
        <el-button
          type="text"
          :icon="ChatRounded"
          @click="$emit('comment', post.id)"
        >
          评论 ({{ post.commentsCount }})
        </el-button>
        <el-button
          type="text"
          :icon="ShareRounded"
          @click="$emit('repost', post.id)"
        >
          转发 ({{ post.repostsCount }})
        </el-button>
        <el-button
          type="text"
          :icon="post.isLiked ? 'HeartFilled' : 'HeartRounded'"
          :class="{ liked: post.isLiked }"
          @click="$emit('like', post.id)"
        >
          点赞 ({{ post.likesCount }})
        </el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { Post } from '../types/post'

interface Props {
  posts: Post[]
}

const props = defineProps<Props>()
const emit = defineEmits<{
  (e: 'like', postId: number): void
  (e: 'comment', postId: number): void
  (e: 'repost', postId: number): void
}>()

const formatTime = (time: string): string => {
  const now = new Date()
  const postTime = new Date(time)
  const diff = now.getTime() - postTime.getTime()
  
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
    return postTime.toLocaleDateString()
  }
}
</script>

<style scoped>
.post-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.post-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
}

.post-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.author-info {
  margin-left: 15px;
}

.author-name {
  font-weight: bold;
  font-size: 16px;
  color: #333;
}

.post-time {
  font-size: 12px;
  color: #999;
  margin-top: 3px;
}

.post-content {
  font-size: 15px;
  line-height: 1.6;
  color: #333;
  margin-bottom: 15px;
  white-space: pre-wrap;
  word-break: break-word;
}

.post-files {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 10px;
  margin-bottom: 15px;
}

.file-item {
  border-radius: 4px;
  overflow: hidden;
}

.post-image {
  width: 100%;
  height: 200px;
  cursor: zoom-in;
}

.post-actions {
  display: flex;
  justify-content: space-around;
  padding-top: 15px;
  border-top: 1px solid #f0f0f0;
}

.post-actions .el-button {
  color: #666;
  font-size: 14px;
}

.post-actions .el-button:hover {
  color: #1890ff;
}

.post-actions .el-button.liked {
  color: #ff4d4f;
}
</style>