package com.weibo.repository;

import com.weibo.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 通知数据访问层
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * 根据用户ID查询通知列表
     */
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    /**
     * 根据用户ID查询未读通知列表
     */
    List<Notification> findByUserIdAndIsReadOrderByCreatedAtDesc(Long userId, Integer isRead);

    /**
     * 根据用户ID统计未读通知数量
     */
    Long countByUserIdAndIsRead(Long userId, Integer isRead);

    /**
     * 将用户的所有通知标记为已读
     */
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = :isRead WHERE n.userId = :userId")
    void updateByUserIdAndIsRead(@Param("userId") Long userId, @Param("isRead") Integer isRead);
}
