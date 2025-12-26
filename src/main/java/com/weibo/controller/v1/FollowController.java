package com.weibo.controller.v1;

import com.weibo.service.AuthService;
import com.weibo.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 关注控制器
 */
@RestController
@RequestMapping("/v1/follows")
@Tag(name = "关注管理", description = "用户关注关系相关接口")
public class FollowController {

    @Autowired
    private FollowService followService;

    @Autowired
    private AuthService authService;

    @PostMapping("/following/{followingId}")
    @Operation(summary = "关注用户", description = "关注指定用户")
    public ResponseEntity<Map<String, Object>> follow(@PathVariable(name = "followingId") Long followingId) {
        Long followerId = authService.getCurrentUser().getId();
        followService.follow(followerId, followingId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "关注成功");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/following/{followingId}")
    @Operation(summary = "取消关注用户", description = "取消关注指定用户")
    public ResponseEntity<Map<String, Object>> unfollow(@PathVariable(name = "followingId") Long followingId) {
        Long followerId = authService.getCurrentUser().getId();
        followService.unfollow(followerId, followingId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "取消关注成功");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{followerId}/following/{followingId}")
    @Operation(summary = "检查是否已关注", description = "检查用户是否已关注指定用户")
    public ResponseEntity<Map<String, Object>> isFollowing(@PathVariable(name = "followerId") Long followerId, @PathVariable(name = "followingId") Long followingId) {
        boolean isFollowing = followService.isFollowing(followerId, followingId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", isFollowing);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/following")
    @Operation(summary = "获取关注列表", description = "获取用户的关注列表")
    public ResponseEntity<Map<String, Object>> getFollowing(@PathVariable(name = "userId") Long userId) {
        var followingList = followService.getFollowing(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", followingList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/followers")
    @Operation(summary = "获取粉丝列表", description = "获取用户的粉丝列表")
    public ResponseEntity<Map<String, Object>> getFollowers(@PathVariable(name = "userId") Long userId) {
        var followersList = followService.getFollowers(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", followersList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/following-count")
    @Operation(summary = "获取关注数", description = "获取用户的关注数")
    public ResponseEntity<Map<String, Object>> getFollowingCount(@PathVariable(name = "userId") Long userId) {
        Long count = followService.getFollowingCount(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", count);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/followers-count")
    @Operation(summary = "获取粉丝数", description = "获取用户的粉丝数")
    public ResponseEntity<Map<String, Object>> getFollowersCount(@PathVariable(name = "userId") Long userId) {
        Long count = followService.getFollowersCount(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", count);
        return ResponseEntity.ok(response);
    }
}