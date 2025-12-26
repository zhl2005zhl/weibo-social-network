package com.weibo.repository;

import com.weibo.entity.Follow;
import com.weibo.entity.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 关注数据访问层
 */
public interface FollowRepository extends JpaRepository<Follow, FollowId> {

    /**
     * 根据关注者ID和被关注者ID查询关注记录
     */
    Optional<Follow> findByIdFollowerIdAndIdFollowingId(Long followerId, Long followingId);

    /**
     * 根据关注者ID查询关注列表
     */
    List<Follow> findByIdFollowerIdOrderByCreatedAtDesc(Long followerId);

    /**
     * 根据被关注者ID查询粉丝列表
     */
    List<Follow> findByIdFollowingIdOrderByCreatedAtDesc(Long followingId);

    /**
     * 根据关注者ID统计关注数量
     */
    Long countByIdFollowerId(Long followerId);

    /**
     * 根据被关注者ID统计粉丝数量
     */
    Long countByIdFollowingId(Long followingId);

    /**
     * 删除关注记录
     */
    void deleteByIdFollowerIdAndIdFollowingId(Long followerId, Long followingId);

    /**
     * 检查用户是否已关注
     */
    boolean existsByIdFollowerIdAndIdFollowingId(Long followerId, Long followingId);
}