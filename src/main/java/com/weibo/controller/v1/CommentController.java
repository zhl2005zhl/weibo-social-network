package com.weibo.controller.v1;

import com.weibo.dto.CommentDTO;
import com.weibo.entity.Comment;
import com.weibo.service.AuthService;
import com.weibo.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论控制器
 */
@RestController
@RequestMapping("/v1/comments")
@Tag(name = "评论管理", description = "动态评论相关接口")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private AuthService authService;

    @PostMapping("/posts/{postId}")
    @Operation(summary = "创建评论", description = "在指定动态下创建评论")
    public ResponseEntity<Map<String, Object>> createComment(@PathVariable(name = "postId") Long postId, @Valid @RequestBody CommentDTO commentDTO) {
        Long userId = authService.getCurrentUser().getId();
        Comment comment = commentService.createComment(commentDTO, postId, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "评论成功");
        response.put("data", comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取评论详情", description = "根据评论ID获取评论详情")
    public ResponseEntity<Map<String, Object>> getCommentById(@PathVariable(name = "id") Long id) {
        Comment comment = commentService.getCommentById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", comment);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新评论", description = "根据评论ID更新评论")
    public ResponseEntity<Map<String, Object>> updateComment(@PathVariable(name = "id") Long id, @Valid @RequestBody CommentDTO commentDTO) {
        Long userId = authService.getCurrentUser().getId();
        Comment updatedComment = commentService.updateComment(id, commentDTO, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "更新成功");
        response.put("data", updatedComment);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除评论", description = "根据评论ID删除评论")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable(name = "id") Long id) {
        Long userId = authService.getCurrentUser().getId();
        commentService.deleteComment(id, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/{postId}")
    @Operation(summary = "获取动态的评论列表", description = "根据动态ID获取评论列表")
    public ResponseEntity<Map<String, Object>> getCommentsByPostId(@PathVariable(name = "postId") Long postId) {
        List<Comment> comments = commentService.getCommentsByPostId(postId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", comments);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "点赞评论", description = "点赞指定评论")
    public ResponseEntity<Map<String, Object>> likeComment(@PathVariable(name = "id") Long id) {
        Long userId = authService.getCurrentUser().getId();
        commentService.likeComment(id, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "点赞成功");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/like")
    @Operation(summary = "取消点赞评论", description = "取消点赞指定评论")
    public ResponseEntity<Map<String, Object>> unlikeComment(@PathVariable(name = "id") Long id) {
        Long userId = authService.getCurrentUser().getId();
        commentService.unlikeComment(id, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "取消点赞成功");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/is-liked")
    @Operation(summary = "检查是否已点赞评论", description = "检查用户是否已点赞指定评论")
    public ResponseEntity<Map<String, Object>> isLiked(@PathVariable(name = "id") Long id) {
        Long userId = authService.getCurrentUser().getId();
        boolean isLiked = commentService.isLiked(id, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", isLiked);
        return ResponseEntity.ok(response);
    }
}