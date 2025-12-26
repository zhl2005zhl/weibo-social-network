// 用户关系类型
export interface Follow {
  id?: number
  followerId: number
  followingId: number
  createdAt: string
  follower?: {
    id: number
    username: string
    nickname: string
    avatarId?: number
  }
  following?: {
    id: number
    username: string
    nickname: string
    avatarId?: number
  }
}

// 用户统计信息类型
export interface UserStats {
  followingCount: number
  followersCount: number
  postsCount: number
}
