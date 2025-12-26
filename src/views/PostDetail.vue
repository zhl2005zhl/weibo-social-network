<template>
  <div class="post-detail-container">
    <el-row :gutter="20">
      <el-col :span="24" class="main-content">
        <div class="post-detail" v-if="postDetail">
          <div class="post-header">
            <el-avatar :src="postDetail.author?.avatarId ? `http://localhost:8080/api/files/${postDetail.author.avatarId}` : ''" size="large">
              {{ postDetail.author?.nickname?.charAt(0) || 'U' }}
            </el-avatar>
            <div class="author-info">
              <div class="author-name">{{ postDetail.author?.nickname }}</div>
              <div class="post-time">{{ formatTime(postDetail.createdAt) }}</div>
            </div>
          </div>
          <div class="post-content">{{ postDetail.content }}</div>
          <div class="post-files" v-if="postDetail.files && postDetail.files.length > 0">
            <div class="file-item" v-for="file in postDetail.files" :key="file.id">
              <el-image :src="file.url" fit="cover" class="post-image" preview-teleported />
            </div>
          </div>
          <div class="post-actions">
            <el-button
              type="text"
              :icon="ChatRounded"
            >
              评论 ({{ postDetail.commentsCount }})
            </el-button>
            <el-button
              type="text"
              :icon="ShareRounded"
              @click="handleRepost"
            >
              转发 ({{ postDetail.repostsCount }})
            </el-button>
            <el-button
              type="text"
              :icon="postDetail.isLiked ? 'HeartFilled' : 'HeartRounded'"
              :class="{ liked: postDetail.isLiked }"
              @click="handleLike"
            >
              点赞 ({{ postDetail.likesCount }})
            </el-button>
          </div>
        </div>

        <el-divider />

        <!-- 评论区 -->
        <div class="comments-section">
          <h3>评论 ({{ comments.length }})</h3>
          <el-form ref="commentFormRef" :model="commentForm" :rules="commentRules">
            <el-form-item prop="content">
              <el-input
                v-model="commentForm.content"
                placeholder="写下你的评论..."
                type="textarea"
                :rows="2"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                :loading="loading"
                @click="handleSubmitComment"
                class="comment-btn"
              >
                发表评论
              </el-button>
            </el-form-item>
          </el-form>

          <!-- 评论列表 -->
          <div class="comments-list">
            <div v-for="comment in comments" :key="comment.id" class="comment-item">
              <div class="comment-header">
                <el-avatar :src="comment.author?.avatarId ? `http://localhost:8080/api/files/${comment.author.avatarId}` : ''" size="small">
                  {{ comment.author?.nickname?.charAt(0) || 'U' }}
                </el-avatar>
                <div class="comment-author-info">
                  <div class="comment-author-name">{{ comment.author?.nickname }}</div>
                  <div class="comment-time">{{ formatTime(comment.createdAt) }}</div>
                </div>
              </div>
              <div class="comment-content">{{ comment.content }}</div>
              <div class="comment-actions">
                <el-button
                  type="text"
                  size="small"
                  @click="replyToComment(comment.id)"
                >
                  回复
                </el-button>
                <el-button
                  type="text"
                  size="small"
                  :icon="comment.isLiked ? 'HeartFilled' : 'HeartRounded'"
                  :class="{ liked: comment.isLiked }"
                >
                  点赞 ({{ comment.likesCount }})
                </el-button>
              </div>
              <!-- 回复输入框 -->
              <div v-if="replyingTo === comment.id" class="reply-form">
                <el-input
                  v-model="replyForm.content"
                  placeholder="回复评论..."
                  type="textarea"
                  :rows="2"
                />
                <div class="reply-actions">
                  <el-button type="primary" size="small" @click="submitReply(comment.id)">发表回复</el-button>
                  <el-button type="text" size="small" @click="cancelReply">取消</el-button>
                </div>
              </div>
              <!-- 回复列表 -->
              <div class="replies-list" v-if="comment.replies && comment.replies.length > 0">
                <div v-for="reply in comment.replies" :key="reply.id" class="reply-item">
                  <div class="comment-header">
                    <el-avatar :src="reply.author?.avatarId ? `http://localhost:8080/api/files/${reply.author.avatarId}` : ''" size="small">
                      {{ reply.author?.nickname?.charAt(0) || 'U' }}
                    </el-avatar>
                    <div class="comment-author-info">
                      <div class="comment-author-name">{{ reply.author?.nickname }}</div>
                      <div class="comment-time">{{ formatTime(reply.createdAt) }}</div>
                    </div>
                  </div>
                  <div class="comment-content">@{{ comment.author?.nickname }} {{ reply.content }}</div>
                  <div class="comment-actions">
                    <el-button
                      type="text"
                      size="small"
                      @click="replyToComment(reply.id, comment.id)"
                    >
                      回复
                    </el-button>
                    <el-button
                      type="text"
                      size="small"
                      :icon="reply.isLiked ? 'HeartFilled' : 'HeartRounded'"
                      :class="{ liked: reply.isLiked }"
                    >
                      点赞 ({{ reply.likesCount }})
                    </el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { usePostStore } from '../stores/post'
import type { FormInstance, FormRules } from 'element-plus'

const route = useRoute()
const router = useRouter()
const postStore = usePostStore()

const commentFormRef = ref<FormInstance>()
const loading = ref(false)
const postId = computed(() => Number(route.params.id))

const postDetail = computed(() => postStore.postDetail)
const comments = computed(() => postStore.comments)

const commentForm = ref({
  content: ''
})

const replyForm = ref({
  content: ''
})

const replyingTo = ref<number | null>(null)
const replyToParentId = ref<number | null>(null)

const commentRules = ref<FormRules>({
  content: [
    { required: true, message: '请输入评论内容', trigger: 'blur' },
    { max: 1000, message: '评论内容不能超过1000字', trigger: 'blur' }
  ]
})

const formatTime = (time: string): string => {
  const now = new Date()
  const commentTime = new Date(time)
  const diff = now.getTime() - commentTime.getTime()
  
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
    return commentTime.toLocaleDateString()
  }
}

const handleLike = () => {
  if (postDetail.value) {
    postStore.toggleLike(postDetail.value.id)
  }
}

const handleRepost = () => {
  ElMessage.info('转发功能开发中')
}

const handleSubmitComment = async () => {
  if (!commentFormRef.value) return
  try {
    await commentFormRef.value.validate()
    loading.value = true
    await postStore.addComment(postId.value, {
      content: commentForm.value.content
    })
    ElMessage.success('评论成功')
    commentForm.value.content = ''
  } catch (error: any) {
    ElMessage.error(error.message || '评论失败')
  } finally {
    loading.value = false
  }
}

const replyToComment = (commentId: number, parentId?: number) => {
  replyingTo.value = commentId
  replyToParentId.value = parentId || null
}

const submitReply = async (commentId: number) => {
  if (!replyForm.value.content.trim()) {
    ElMessage.warning('请输入回复内容')
    return
  }
  try {
    loading.value = true
    await postStore.addComment(postId.value, {
      content: replyForm.value.content,
      parentId: commentId
    })
    ElMessage.success('回复成功')
    replyForm.value.content = ''
    replyingTo.value = null
    replyToParentId.value = null
  } catch (error: any) {
    ElMessage.error(error.message || '回复失败')
  } finally {
    loading.value = false
  }
}

const cancelReply = () => {
  replyingTo.value = null
  replyToParentId.value = null
  replyForm.value.content = ''
}

onMounted(async () => {
  await postStore.fetchPostDetail(postId.value)
  await postStore.fetchComments(postId.value)
})
</script>

<style scoped>
.post-detail-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
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

.comments-section {
  margin-top: 20px;
}

.comments-section h3 {
  margin-bottom: 20px;
  font-size: 18px;
  color: #333;
}

.comment-btn {
  float: right;
}

.comments-list {
  margin-top: 30px;
}

.comment-item {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #f0f0f0;
}

.comment-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.comment-header {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.comment-author-info {
  margin-left: 10px;
}

.comment-author-name {
  font-weight: bold;
  font-size: 14px;
  color: #333;
}

.comment-time {
  font-size: 12px;
  color: #999;
  margin-top: 2px;
}

.comment-content {
  font-size: 14px;
  line-height: 1.5;
  color: #333;
  margin-bottom: 10px;
  white-space: pre-wrap;
  word-break: break-word;
}

.comment-actions {
  display: flex;
  gap: 20px;
}

.comment-actions .el-button {
  color: #666;
  font-size: 12px;
}

.comment-actions .el-button:hover {
  color: #1890ff;
}

.comment-actions .el-button.liked {
  color: #ff4d4f;
}

.reply-form {
  margin-top: 15px;
  padding: 15px;
  background: #f9f9f9;
  border-radius: 4px;
}

.reply-actions {
  margin-top: 10px;
  text-align: right;
}

.replies-list {
  margin-top: 15px;
  margin-left: 30px;
  padding-left: 15px;
  border-left: 1px solid #e0e0e0;
}

.reply-item {
  margin-bottom: 15px;
}
</style>