package com.weibo.controller.v1;

import com.weibo.entity.User;
import com.weibo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/v1/users")
@Tag(name = "用户管理", description = "用户信息管理相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息")
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.getUserById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新用户信息", description = "根据用户ID更新用户信息")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable(name = "id") Long id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "更新成功");
        response.put("data", updatedUser);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户", description = "根据用户ID删除用户")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "搜索用户", description = "根据关键字搜索用户")
    public ResponseEntity<Map<String, Object>> searchUsers(@RequestParam(name = "keyword") String keyword) {
        List<User> users = userService.searchUsers(keyword);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "搜索成功");
        response.put("data", users);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/following-count")
    @Operation(summary = "获取关注数", description = "根据用户ID获取关注数")
    public ResponseEntity<Map<String, Object>> getFollowingCount(@PathVariable(name = "id") Long id) {
        Long count = userService.getFollowingCount(id);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", count);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/followers-count")
    @Operation(summary = "获取粉丝数", description = "根据用户ID获取粉丝数")
    public ResponseEntity<Map<String, Object>> getFollowersCount(@PathVariable(name = "id") Long id) {
        Long count = userService.getFollowersCount(id);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", count);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{followerId}/is-following/{followingId}")
    @Operation(summary = "检查是否已关注", description = "检查用户是否已关注另一个用户")
    public ResponseEntity<Map<String, Object>> isFollowing(@PathVariable(name = "followerId") Long followerId, @PathVariable(name = "followingId") Long followingId) {
        boolean isFollowing = userService.isFollowing(followerId, followingId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", isFollowing);
        return ResponseEntity.ok(response);
    }
}