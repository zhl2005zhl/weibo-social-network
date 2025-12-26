package com.weibo.service;

import com.weibo.entity.Notification;

import java.util.List;

/**
 * 通知服务接口
 */
public interface NotificationService {

    /**
     * 发送通知
     */
    void sendNotification(Long userId, Long senderId, Integer type, String content, Long targetId);

    /**
     * 根据ID获取通知
     */
    Notification getNotificationById(Long id);

    /**
     * 获取用户的通知列表
     */
    List<Notification> getNotificationsByUserId(Long userId);

    /**
     * 获取用户的未读通知列表
     */
    List<Notification> getUnreadNotificationsByUserId(Long userId);

    /**
     * 将通知标记为已读
     */
    void markAsRead(Long id, Long userId);

    /**
     * 将所有通知标记为已读
     */
    void markAllAsRead(Long userId);

    /**
     * 获取未读通知数量
     */
    Long getUnreadCount(Long userId);
}
