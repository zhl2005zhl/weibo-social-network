package com.weibo.service;

import com.weibo.entity.Follow;

import java.util.List;

/**
 * 关注服务接口
 */
public interface FollowService {

    /**
     * 关注用户
     */
    void follow(Long followerId, Long followingId);

    /**
     * 取消关注用户
     */
    void unfollow(Long followerId, Long followingId);

    /**
     * 检查是否已关注
     */
    boolean isFollowing(Long followerId, Long followingId);

    /**
     * 获取关注列表
     */
    List<Follow> getFollowing(Long followerId);

    /**
     * 获取粉丝列表
     */
    List<Follow> getFollowers(Long followingId);

    /**
     * 获取关注数
     */
    Long getFollowingCount(Long followerId);

    /**
     * 获取粉丝数
     */
    Long getFollowersCount(Long followingId);
}
