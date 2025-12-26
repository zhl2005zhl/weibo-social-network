package com.weibo.controller.v1;

import com.weibo.service.AuthService;
import com.weibo.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 通知控制器
 */
@RestController
@RequestMapping("/v1/notifications")
@Tag(name = "通知管理", description = "用户通知相关接口")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuthService authService;

    @GetMapping
    @Operation(summary = "获取通知列表", description = "获取当前用户的通知列表")
    public ResponseEntity<Map<String, Object>> getNotifications() {
        Long userId = authService.getCurrentUser().getId();
        var notifications = notificationService.getNotificationsByUserId(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", notifications);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/unread")
    @Operation(summary = "获取未读通知列表", description = "获取当前用户的未读通知列表")
    public ResponseEntity<Map<String, Object>> getUnreadNotifications() {
        Long userId = authService.getCurrentUser().getId();
        var notifications = notificationService.getUnreadNotificationsByUserId(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", notifications);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "标记通知为已读", description = "将指定通知标记为已读")
    public ResponseEntity<Map<String, Object>> markAsRead(@PathVariable(name = "id") Long id) {
        Long userId = authService.getCurrentUser().getId();
        notificationService.markAsRead(id, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "标记成功");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/read-all")
    @Operation(summary = "标记所有通知为已读", description = "将当前用户的所有通知标记为已读")
    public ResponseEntity<Map<String, Object>> markAllAsRead() {
        Long userId = authService.getCurrentUser().getId();
        notificationService.markAllAsRead(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "标记成功");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/unread-count")
    @Operation(summary = "获取未读通知数量", description = "获取当前用户的未读通知数量")
    public ResponseEntity<Map<String, Object>> getUnreadCount() {
        Long userId = authService.getCurrentUser().getId();
        Long count = notificationService.getUnreadCount(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", count);
        return ResponseEntity.ok(response);
    }
}