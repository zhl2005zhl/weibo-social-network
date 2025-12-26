<template>
  <div class="user-profile-container">
    <el-container>
      <el-main>
        <!-- 用户信息卡片 -->
        <el-card class="user-info-card">
          <div class="user-info">
            <el-avatar :size="120">
              {{ userInfo?.nickname?.charAt(0) || 'U' }}
            </el-avatar>
            <div class="user-details">
              <h2 class="user-nickname">{{ userInfo?.nickname }}</h2>
              <div class="user-username">@{{ userInfo?.username }}</div>
              <div class="user-bio">{{ userInfo?.bio }}</div>
              <div class="user-meta">
                <span v-if="userInfo?.location" class="meta-item">
                  <el-icon><Location /></el-icon> {{ userInfo.location }}
                </span>
                <span v-if="userInfo?.website" class="meta-item">
                  <el-icon><Link /></el-icon> {{ userInfo.website }}
                </span>
                <span class="meta-item">
                  <el-icon><Calendar /></el-icon> 加入于 {{ formatDate(userInfo?.createdAt) }}
                </span>
              </div>
              <div class="user-stats">
                <div class="stat-item">
                  <span class="stat-number">{{ userStats.postsCount }}</span>
                  <span class="stat-label">动态</span>
                </div>
                <div class="stat-item" @click="activeTab = 'following'">
                  <span class="stat-number">{{ userStats.followingCount }}</span>
                  <span class="stat-label">关注</span>
                </div>
                <div class="stat-item" @click="activeTab = 'followers'">
                  <span class="stat-number">{{ userStats.followersCount }}</span>
                  <span class="stat-label">粉丝</span>
                </div>
              </div>
              <el-button
                v-if="currentUserId !== userInfo?.id"
                type="primary"
                :loading="loading"
                @click="handleFollow"
                :class="{ 'unfollow-btn': isFollowing }"
              >
                {{ isFollowing ? '取消关注' : '关注' }}
              </el-button>
            </div>
          </div>
        </el-card>

        <!-- 内容标签页 -->
        <el-tabs v-model="activeTab" class="content-tabs">
          <el-tab-pane label="动态" name="posts">
            <post-list v-if="hasPosts" :posts="posts" @like="handleLike" @comment="handleComment" @repost="handleRepost" />
            <el-empty v-else description="暂无动态" />
          </el-tab-pane>
          <el-tab-pane label="关注" name="following">
            <div class="follow-list" v-if="followingList.length > 0">
              <div class="follow-item" v-for="follow in followingList" :key="follow.id">
                <el-avatar :size="60">
                  {{ follow.following?.nickname?.charAt(0) || 'U' }}
                </el-avatar>
                <div class="follow-info">
                  <div class="follow-name">{{ follow.following?.nickname }}</div>
                  <div class="follow-username">@{{ follow.following?.username }}</div>
                </div>
                <el-button
                  type="primary"
                  size="small"
                  :loading="loading"
                  @click="handleFollowUser(follow.followingId)"
                  :class="{ 'unfollow-btn': isFollowingUser(follow.followingId) }"
                >
                  {{ isFollowingUser(follow.followingId) ? '取消关注' : '关注' }}
                </el-button>
              </div>
            </div>
            <el-empty v-else description="暂无关注" />
          </el-tab-pane>
          <el-tab-pane label="粉丝" name="followers">
            <div class="follow-list" v-if="followersList.length > 0">
              <div class="follow-item" v-for="follow in followersList" :key="follow.id">
                <el-avatar :size="60">
                  {{ follow.follower?.nickname?.charAt(0) || 'U' }}
                </el-avatar>
                <div class="follow-info">
                  <div class="follow-name">{{ follow.follower?.nickname }}</div>
                  <div class="follow-username">@{{ follow.follower?.username }}</div>
                </div>
                <el-button
                  type="primary"
                  size="small"
                  :loading="loading"
                  @click="handleFollowUser(follow.followerId)"
                  :class="{ 'unfollow-btn': isFollowingUser(follow.followerId) }"
                >
                  {{ isFollowingUser(follow.followerId) ? '取消关注' : '关注' }}
                </el-button>
              </div>
            </div>
            <el-empty v-else description="暂无粉丝" />
          </el-tab-pane>
        </el-tabs>

        <!-- 转发对话框 -->
        <repost-dialog
          v-model:visible="repostDialogVisible"
          :post-id="repostPostId"
          @success="handleRepostSuccess"
        />
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Location, Link, Calendar } from '@element-plus/icons-vue'
import { useUserStore } from '../stores/user'
import { usePostStore } from '../stores/post'
import { useFollowStore } from '../stores/follow'
import PostList from '../components/PostList.vue'
import RepostDialog from '../components/RepostDialog.vue'
import type { Follow } from '../types/follow'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const postStore = usePostStore()
const followStore = useFollowStore()

const userId = computed(() => Number(route.params.id))
const currentUserId = computed(() => userStore.userInfo?.id || 0)
const userInfo = computed(() => userStore.userInfo)
const posts = computed(() => postStore.posts)
const hasPosts = computed(() => postStore.hasPosts)
const followingList = computed(() => followStore.followingList)
const followersList = computed(() => followStore.followersList)

const loading = ref(false)
const activeTab = ref('posts')
const isFollowing = ref(false)
const repostDialogVisible = ref(false)
const repostPostId = ref<number | null>(null)

const userStats = ref({
  postsCount: 0,
  followingCount: 0,
  followersCount: 0
})

const formatDate = (dateString?: string): string => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString()
}

const handleFollow = async () => {
  if (!userInfo.value?.id) return
  try {
    loading.value = true
    await followStore.toggleFollow(userInfo.value.id, isFollowing.value)
    isFollowing.value = !isFollowing.value
    // 更新用户统计信息
    if (isFollowing.value) {
      userStats.value.followersCount++
    } else {
      userStats.value.followersCount--
    }
    ElMessage.success(isFollowing.value ? '关注成功' : '取消关注成功')
  } catch (error: any) {
    ElMessage.error(error.message || '操作失败')
  } finally {
    loading.value = false
  }
}

const handleFollowUser = async (targetUserId: number) => {
  try {
    loading.value = true
    const following = followStore.isFollowing(currentUserId.value, targetUserId)
    await followStore.toggleFollow(targetUserId, following)
    // 重新加载关注/粉丝列表
    if (activeTab.value === 'following') {
      await followStore.fetchFollowingList(userId.value)
    } else if (activeTab.value === 'followers') {
      await followStore.fetchFollowersList(userId.value)
    }
    ElMessage.success(following ? '取消关注成功' : '关注成功')
  } catch (error: any) {
    ElMessage.error(error.message || '操作失败')
  } finally {
    loading.value = false
  }
}

const isFollowingUser = (targetUserId: number): boolean => {
  if (activeTab.value === 'following') {
    return followingList.value.some(follow => follow.followingId === targetUserId)
  } else {
    // 对于粉丝列表，需要检查是否已关注该粉丝
    return followStore.followingList.some(follow => follow.followingId === targetUserId)
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
  await fetchUserPosts()
  ElMessage.success('转发成功')
}

const fetchUserPosts = async () => {
  await postStore.fetchPostsByAuthorId(userId.value)
  // 更新动态数量
  userStats.value.postsCount = posts.value.length
}

const fetchUserInfo = async () => {
  // 这里需要获取用户信息，暂时使用模拟数据
  // await userStore.fetchUserById(userId.value)
  await followStore.fetchUserStats(userId.value)
  const stats = followStore.getUserStats(userId.value)
  userStats.value.followingCount = stats.followingCount
  userStats.value.followersCount = stats.followersCount
  
  // 检查是否已关注该用户
  if (currentUserId.value !== userId.value) {
    isFollowing.value = await followStore.checkFollowStatus(currentUserId.value, userId.value)
  }
}

const fetchFollowData = async () => {
  if (activeTab.value === 'following') {
    await followStore.fetchFollowingList(userId.value)
  } else if (activeTab.value === 'followers') {
    await followStore.fetchFollowersList(userId.value)
  }
}

onMounted(async () => {
  await fetchUserInfo()
  await fetchUserPosts()
})

// 监听标签页变化，加载对应数据
watch(activeTab, async (newTab) => {
  if (newTab === 'following' || newTab === 'followers') {
    await fetchFollowData()
  } else if (newTab === 'posts') {
    await fetchUserPosts()
  }
})
</script>

<style scoped>
.user-profile-container {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

.user-info-card {
  margin-bottom: 20px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.user-info {
  display: flex;
  padding: 20px;
  gap: 30px;
}

.user-details {
  flex: 1;
}

.user-nickname {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 8px;
}

.user-username {
  color: #666;
  margin-bottom: 16px;
  font-size: 16px;
}

.user-bio {
  margin-bottom: 16px;
  line-height: 1.5;
  color: #333;
}

.user-meta {
  margin-bottom: 20px;
  display: flex;
  gap: 20px;
  color: #666;
  font-size: 14px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

.user-stats {
  display: flex;
  gap: 30px;
  margin-bottom: 20px;
}

.stat-item {
  cursor: pointer;
}

.stat-number {
  font-size: 20px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 14px;
  color: #666;
  margin-left: 5px;
}

.unfollow-btn {
  background-color: #fff;
  color: #666;
  border-color: #d9d9d9;
}

.unfollow-btn:hover {
  color: #ff4d4f;
  border-color: #ff4d4f;
  background-color: #fff1f0;
}

.content-tabs {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.follow-list {
  padding: 20px;
}

.follow-item {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
}

.follow-item:last-child {
  border-bottom: none;
}

.follow-info {
  flex: 1;
}

.follow-name {
  font-weight: bold;
  font-size: 16px;
  color: #333;
}

.follow-username {
  color: #666;
  font-size: 14px;
  margin-top: 3px;
}
</style>