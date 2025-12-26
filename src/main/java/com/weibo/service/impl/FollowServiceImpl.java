package com.weibo.service.impl;

import com.weibo.entity.Follow;
import com.weibo.entity.FollowId;
import com.weibo.enums.NotificationType;
import com.weibo.repository.FollowRepository;
import com.weibo.service.FollowService;
import com.weibo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 关注服务实现类
 */
@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public void follow(Long followerId, Long followingId) {
        // 检查是否已关注
        if (isFollowing(followerId, followingId)) {
            throw new RuntimeException("已经关注过该用户");
        }

        // 不能关注自己
        if (followerId.equals(followingId)) {
            throw new RuntimeException("不能关注自己");
        }

        FollowId followId = new FollowId(followerId, followingId);
        Follow follow = new Follow();
        follow.setId(followId);
        followRepository.save(follow);

        // 发送关注通知
        notificationService.sendNotification(
                followingId,
                followerId,
                NotificationType.FOLLOW.getValue(),
                "关注了你",
                followerId
        );
    }

    @Override
    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        // 检查是否已关注
        if (!isFollowing(followerId, followingId)) {
            throw new RuntimeException("还没有关注该用户");
        }

        followRepository.deleteByIdFollowerIdAndIdFollowingId(followerId, followingId);
    }

    @Override
    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsByIdFollowerIdAndIdFollowingId(followerId, followingId);
    }

    @Override
    public List<Follow> getFollowing(Long followerId) {
        return followRepository.findByIdFollowerIdOrderByCreatedAtDesc(followerId);
    }

    @Override
    public List<Follow> getFollowers(Long followingId) {
        return followRepository.findByIdFollowingIdOrderByCreatedAtDesc(followingId);
    }

    @Override
    public Long getFollowingCount(Long followerId) {
        return followRepository.countByIdFollowerId(followerId);
    }

    @Override
    public Long getFollowersCount(Long followingId) {
        return followRepository.countByIdFollowingId(followingId);
    }
}
