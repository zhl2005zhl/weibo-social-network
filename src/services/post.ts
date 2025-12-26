import apiClient from './api'
import type { Post, PostRequest, Comment, CommentRequest } from '../types/post'

/**
 * 创建动态
 */
export const createPost = (data: FormData): Promise<Post> => {
  return apiClient.post('/v1/posts', data)
}

/**
 * 获取动态详情
 */
export const getPostById = (id: number): Promise<Post> => {
  return apiClient.get(`/v1/posts/${id}`)
}

/**
 * 更新动态
 */
export const updatePost = (id: number, data: FormData): Promise<Post> => {
  return apiClient.put(`/v1/posts/${id}`, data, {
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

/**
 * 删除动态
 */
export const deletePost = (id: number): Promise<void> => {
  return apiClient.delete(`/v1/posts/${id}`)
}

/**
 * 获取用户的动态列表
 */
export const getPostsByAuthorId = (authorId: number): Promise<Post[]> => {
  return apiClient.get(`/v1/posts/author/${authorId}`)
}

/**
 * 获取时间线
 */
export const getTimeline = (authorIds: number[]): Promise<Post[]> => {
  return apiClient.get('/v1/posts/timeline', {
    params: {
      authorIds: authorIds.join(',')
    }
  })
}

/**
 * 获取热门动态
 */
export const getHotPosts = (): Promise<Post[]> => {
  return apiClient.get('/v1/posts/hot')
}

/**
 * 点赞动态
 */
export const likePost = (id: number): Promise<void> => {
  return apiClient.post(`/v1/posts/${id}/like`)
}

/**
 * 取消点赞动态
 */
export const unlikePost = (id: number): Promise<void> => {
  return apiClient.delete(`/v1/posts/${id}/like`)
}

/**
 * 检查是否已点赞动态
 */
export const checkPostLike = (id: number): Promise<boolean> => {
  return apiClient.get(`/v1/posts/${id}/is-liked`)
}

/**
 * 转发动态
 */
export const repost = (id: number, content: string): Promise<Post> => {
  return apiClient.post(`/v1/posts/${id}/repost`, null, {
    params: {
      content
    }
  })
}

/**
 * 获取动态的评论列表
 */
export const getCommentsByPostId = (postId: number): Promise<Comment[]> => {
  return apiClient.get(`/v1/comments/posts/${postId}`)
}

/**
 * 创建评论
 */
export const createComment = (postId: number, data: CommentRequest): Promise<Comment> => {
  return apiClient.post(`/v1/comments/posts/${postId}`, data)
}

/**
 * 点赞评论
 */
export const likeComment = (id: number): Promise<void> => {
  return apiClient.post(`/v1/comments/${id}/like`)
}

/**
 * 取消点赞评论
 */
export const unlikeComment = (id: number): Promise<void> => {
  return apiClient.delete(`/v1/comments/${id}/like`)
}

/**
 * 检查是否已点赞评论
 */
export const checkCommentLike = (id: number): Promise<boolean> => {
  return apiClient.get(`/v1/comments/${id}/is-liked`)
}
