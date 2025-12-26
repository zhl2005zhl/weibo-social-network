<template>
  <el-dialog
    v-model="dialogVisible"
    title="转发"
    width="500px"
  >
    <el-form ref="repostFormRef" :model="repostForm" :rules="repostRules">
      <el-form-item prop="content">
        <el-input
          v-model="repostForm.content"
          placeholder="写下你的转发理由..."
          type="textarea"
          :rows="3"
          maxlength="2000"
          show-word-limit
        />
      </el-form-item>
      <!-- 原动态预览 -->
      <div class="original-post" v-if="originalPost">
        <el-divider content-position="left">原动态</el-divider>
        <div class="post-content">{{ originalPost.content }}</div>
        <div class="post-actions">
          <span>点赞 {{ originalPost.likesCount }} | 评论 {{ originalPost.commentsCount }} | 转发 {{ originalPost.repostsCount }}</span>
        </div>
      </div>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="loading" @click="handleSubmit">转发</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { usePostStore } from '../stores/post'
import type { FormInstance, FormRules } from 'element-plus'
import type { Post } from '../types/post'

interface Props {
  visible: boolean
  postId: number | null
}

const props = defineProps<Props>()
const emit = defineEmits<{
  (e: 'update:visible', value: boolean): void
  (e: 'success'): void
}>()

const dialogVisible = ref(props.visible)
const repostFormRef = ref<FormInstance>()
const loading = ref(false)
const postStore = usePostStore()
const originalPost = ref<Post | null>(null)

const repostForm = ref({
  content: ''
})

const repostRules = ref<FormRules>({
  content: [
    { max: 2000, message: '内容不能超过2000字', trigger: 'blur' }
  ]
})

// 监听visible变化
watch(() => props.visible, (newVal) => {
  dialogVisible.value = newVal
  if (newVal && props.postId) {
    fetchOriginalPost()
  }
})

watch(() => dialogVisible.value, (newVal) => {
  emit('update:visible', newVal)
})

const fetchOriginalPost = async () => {
  if (props.postId) {
    const post = await postStore.fetchPostDetail(props.postId)
    originalPost.value = post
  }
}

const handleSubmit = async () => {
  if (!repostFormRef.value) return
  if (!props.postId) return
  
  try {
    await repostFormRef.value.validate()
    loading.value = true
    
    // 调用转发API
    await import('../services/post').then(postApi => {
      return postApi.repost(props.postId!, repostForm.value.content)
    })
    
    ElMessage.success('转发成功')
    emit('success')
    dialogVisible.value = false
    repostForm.value.content = ''
  } catch (error: any) {
    ElMessage.error(error.message || '转发失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.original-post {
  margin: 10px 0;
  padding: 10px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.post-content {
  font-size: 14px;
  line-height: 1.5;
  margin-bottom: 10px;
}

.post-actions {
  font-size: 12px;
  color: #909399;
}
</style>