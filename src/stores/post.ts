import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import * as postApi from '../services/post'
import type { Post, Comment } from '../types/post'

interface PostState {
  posts: Post[]
  postDetail: Post | null
  comments: Comment[]
  loading: boolean
  error: string | null
}

export const usePostStore = defineStore('post', () => {
  // 状态
  const posts = ref<Post[]>([])
  const postDetail = ref<Post | null>(null)
  const comments = ref<Comment[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)
  
  // 计算属性
  const totalPosts = computed(() => posts.value.length)
  const hasPosts = computed(() => totalPosts.value > 0)
  
  // 动作
  const fetchPostsByAuthor = async (authorId: number) => {
    loading.value = true
    error.value = null
    try {
      const data = await postApi.getPostsByAuthorId(authorId)
      posts.value = data
      return data
    } catch (err: any) {
      error.value = err.message
      return []
    } finally {
      loading.value = false
    }
  }
  
  const fetchTimeline = async (authorIds: number[]) => {
    loading.value = true
    error.value = null
    try {
      const data = await postApi.getTimeline(authorIds)
      posts.value = data
      return data
    } catch (err: any) {
      error.value = err.message
      return []
    } finally {
      loading.value = false
    }
  }
  
  const fetchHotPosts = async () => {
    loading.value = true
    error.value = null
    try {
      const data = await postApi.getHotPosts()
      posts.value = data
      return data
    } catch (err: any) {
      error.value = err.message
      return []
    } finally {
      loading.value = false
    }
  }
  
  const fetchPostDetail = async (id: number) => {
    loading.value = true
    error.value = null
    try {
      const data = await postApi.getPostById(id)
      postDetail.value = data
      return data
    } catch (err: any) {
      error.value = err.message
      return null
    } finally {
      loading.value = false
    }
  }
  
  const createNewPost = async (data: FormData) => {
    loading.value = true
    error.value = null
    try {
      const result = await postApi.createPost(data)
      posts.value.unshift(result)
      return result
    } catch (err: any) {
      error.value = err.message
      return null
    } finally {
      loading.value = false
    }
  }
  
  const deletePostById = async (id: number) => {
    loading.value = true
    error.value = null
    try {
      await postApi.deletePost(id)
      posts.value = posts.value.filter(post => post.id !== id)
      if (postDetail.value?.id === id) {
        postDetail.value = null
      }
    } catch (err: any) {
      error.value = err.message
    } finally {
      loading.value = false
    }
  }
  
  const toggleLike = async (id: number) => {
    const post = posts.value.find(p => p.id === id)
    if (post) {
      if (post.isLiked) {
        await postApi.unlikePost(id)
        post.isLiked = false
        post.likesCount--
      } else {
        await postApi.likePost(id)
        post.isLiked = true
        post.likesCount++
      }
    }
  }
  
  const fetchComments = async (postId: number) => {
    loading.value = true
    error.value = null
    try {
      const data = await postApi.getCommentsByPostId(postId)
      comments.value = data
      return data
    } catch (err: any) {
      error.value = err.message
      return []
    } finally {
      loading.value = false
    }
  }
  
  const addComment = async (postId: number, data: { content: string; parentId?: number }) => {
    loading.value = true
    error.value = null
    try {
      const result = await postApi.createComment(postId, data)
      comments.value.unshift(result)
      // 更新动态的评论数
      const post = posts.value.find(p => p.id === postId)
      if (post) {
        post.commentsCount++
      }
      if (postDetail.value?.id === postId) {
        postDetail.value.commentsCount++
      }
      return result
    } catch (err: any) {
      error.value = err.message
      return null
    } finally {
      loading.value = false
    }
  }
  
  return {
    posts,
    postDetail,
    comments,
    loading,
    error,
    totalPosts,
    hasPosts,
    fetchPostsByAuthor,
    fetchTimeline,
    fetchHotPosts,
    fetchPostDetail,
    createNewPost,
    deletePostById,
    toggleLike,
    fetchComments,
    addComment
  }
})