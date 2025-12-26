// 动态类型
export interface Post {
  id: number
  content: string
  authorId: number
  parentId?: number
  likesCount: number
  commentsCount: number
  repostsCount: number
  createdAt: string
  updatedAt: string
  author?: {
    id: number
    username: string
    nickname: string
    avatarId?: number
  }
  files?: {
    id: number
    url: string
    contentType: string
  }[]
  isLiked?: boolean
}

// 动态请求类型
export interface PostRequest {
  content: string
  parentId?: number
  files?: File[]
}

// 评论类型
export interface Comment {
  id: number
  content: string
  authorId: number
  postId: number
  parentId?: number
  likesCount: number
  createdAt: string
  updatedAt: string
  author?: {
    id: number
    username: string
    nickname: string
    avatarId?: number
  }
  replies?: Comment[]
  isLiked?: boolean
}

// 评论请求类型
export interface CommentRequest {
  content: string
  parentId?: number
}
