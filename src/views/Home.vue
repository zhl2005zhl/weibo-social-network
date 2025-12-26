<template>
  <div class="home-container">
    <div class="main-content">
      <el-container>
        <el-main>
          <!-- 动态发布 -->
          <div class="create-post-card">
            <h2>发布新动态</h2>
            <el-form ref="createPostRef" :model="postForm" :rules="postRules">
              <el-form-item prop="content">
                <el-input
                  v-model="postForm.content"
                  type="textarea"
                  placeholder="写下你的想法..."
                  :rows="3"
                  maxlength="2000"
                  show-word-limit
                />
              </el-form-item>
              <el-form-item>
                <el-upload
                  v-model:file-list="fileList"
                  :auto-upload="false"
                  :multiple="true"
                  :limit="9"
                  accept="image/*"
                  list-type="picture-card"
                >
                  <el-icon><Plus /></el-icon>
                </el-upload>
              </el-form-item>
              <el-form-item>
                <el-button
                  type="primary"
                  :loading="loading"
                  @click="handleSubmit"
                  class="post-btn"
                >
                  发布
                </el-button>
              </el-form-item>
            </el-form>
          </div>

          <!-- 时间线 -->
          <div class="timeline-container">
            <el-tabs v-model="activeTab" class="timeline-tabs">
              <el-tab-pane label="关注" name="following">
                <post-list v-if="hasPosts" :posts="posts" @like="handleLike" @comment="handleComment" @repost="handleRepost" />
                <el-empty v-else description="暂无动态" />
              </el-tab-pane>
              <el-tab-pane label="热门" name="hot">
                <post-list v-if="hasPosts" :posts="posts" @like="handleLike" @comment="handleComment" @repost="handleRepost" />
                <el-empty v-else description="暂无动态" />
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-main>
      </el-container>
    </div>
    <!-- 转发对话框 -->
    <RepostDialog
      v-model:visible="repostDialogVisible"
      :post-id="repostPostId"
      @success="handleRepostSuccess"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'
import { usePostStore } from '../stores/post'
import PostList from '../components/PostList.vue'
import RepostDialog from '../components/RepostDialog.vue'
import type { FormInstance, FormRules, UploadFile } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const postStore = usePostStore()

const createPostRef = ref<FormInstance>()
const loading = ref(false)
const activeTab = ref('following')

import { Plus } from '@element-plus/icons-vue'

const postForm = ref({
  content: ''
})

const fileList = ref<UploadFile[]>([])

const postRules = ref<FormRules>({
  content: [
    { required: true, message: '请输入内容', trigger: 'blur' },
    { max: 2000, message: '内容不能超过2000字', trigger: 'blur' }
  ]
})

const hasPosts = computed(() => postStore.hasPosts)
const posts = computed(() => postStore.posts)

// 转发对话框状态
const repostDialogVisible = ref(false)
const repostPostId = ref<number | null>(null)

const handleSubmit = async () => {
  if (!createPostRef.value) return
  try {
    await createPostRef.value.validate()
    loading.value = true
    
    const formData = new FormData()
    // 创建postDTO对象
    const postDTO = {
      content: postForm.value.content
    }
    // 将postDTO转为JSON字符串并添加到FormData
    formData.append('postDTO', new Blob([JSON.stringify(postDTO)], { type: 'application/json' }))
    // 添加文件
    fileList.value.forEach(file => {
      if (file.raw) {
        formData.append('files', file.raw)
      }
    })
    
    await postStore.createNewPost(formData)
    ElMessage.success('发布成功')
    postForm.value.content = ''
    fileList.value = []
  } catch (error: any) {
    ElMessage.error(error.message || '发布失败')
  } finally {
    loading.value = false
  }
}

const handleLike = (postId: number) => {
  postStore.toggleLike(postId)
}

const handleComment = (postId: number) => {
  router.push(`/post/${postId}`)
}

const handleRepost = (postId: number) => {
  repostPostId.value = postId
  repostDialogVisible.value = true
}

const handleRepostSuccess = async () => {
  // 重新加载动态列表
  await fetchPosts()
  ElMessage.success('转发成功')
}

const fetchPosts = async () => {
  if (activeTab.value === 'following') {
    await postStore.fetchTimeline([userStore.userInfo?.id || 0]) // TODO: 获取关注列表
  } else {
    await postStore.fetchHotPosts()
  }
}

onMounted(async () => {
  await userStore.fetchUserInfo()
  await fetchPosts()
})

watch(activeTab, fetchPosts)
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.create-post {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.post-form {
  margin-bottom: 15px;
}

.upload-btn {
  margin-right: 10px;
}

.post-btn {
  float: right;
}

.timeline-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 0;
}

.timeline-tabs {
  margin-bottom: 0;
}

:deep(.el-tabs__content) {
  padding: 20px;
}
</style>