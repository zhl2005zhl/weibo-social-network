package com.weibo.service;

/**
 * 点赞服务接口
 */
public interface LikeService {

    /**
     * 添加点赞
     */
    void addLike(Long userId, Integer targetType, Long targetId);

    /**
     * 移除点赞
     */
    void removeLike(Long userId, Integer targetType, Long targetId);

    /**
     * 检查用户是否已点赞
     */
    boolean isLiked(Long userId, Integer targetType, Long targetId);

    /**
     * 获取点赞数量
     */
    Long getLikeCount(Integer targetType, Long targetId);
}
