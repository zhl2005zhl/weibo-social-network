package com.weibo.repository;

import com.weibo.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 点赞数据访问层
 */
public interface LikeRepository extends JpaRepository<Like, Long> {

    /**
     * 根据用户ID和目标类型、目标ID查询点赞记录
     */
    Optional<Like> findByUserIdAndTargetTypeAndTargetId(Long userId, Integer targetType, Long targetId);

    /**
     * 根据目标类型和目标ID查询点赞记录列表
     */
    List<Like> findByTargetTypeAndTargetId(Integer targetType, Long targetId);

    /**
     * 根据用户ID查询点赞记录列表
     */
    List<Like> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * 根据目标类型和目标ID统计点赞数量
     */
    Long countByTargetTypeAndTargetId(Integer targetType, Long targetId);

    /**
     * 删除点赞记录
     */
    void deleteByUserIdAndTargetTypeAndTargetId(Long userId, Integer targetType, Long targetId);
}