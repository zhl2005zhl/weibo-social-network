package com.weibo.service.impl;

import com.weibo.entity.Like;
import com.weibo.repository.LikeRepository;
import com.weibo.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 点赞服务实现类
 */
@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Override
    @Transactional
    public void addLike(Long userId, Integer targetType, Long targetId) {
        // 检查是否已点赞
        if (isLiked(userId, targetType, targetId)) {
            throw new RuntimeException("已经点赞过");
        }

        Like like = new Like();
        like.setUserId(userId);
        like.setTargetType(targetType);
        like.setTargetId(targetId);
        likeRepository.save(like);
    }

    @Override
    @Transactional
    public void removeLike(Long userId, Integer targetType, Long targetId) {
        // 检查是否已点赞
        if (!isLiked(userId, targetType, targetId)) {
            throw new RuntimeException("还没有点赞");
        }

        likeRepository.deleteByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId);
    }

    @Override
    public boolean isLiked(Long userId, Integer targetType, Long targetId) {
        return likeRepository.findByUserIdAndTargetTypeAndTargetId(userId, targetType, targetId).isPresent();
    }

    @Override
    public Long getLikeCount(Integer targetType, Long targetId) {
        return likeRepository.countByTargetTypeAndTargetId(targetType, targetId);
    }
}
