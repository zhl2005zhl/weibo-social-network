package com.weibo.controller.v1;

import com.weibo.dto.PostDTO;
import com.weibo.entity.Post;
import com.weibo.service.AuthService;
import com.weibo.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 动态控制器
 */
@RestController
@RequestMapping("/v1/posts")
@Tag(name = "动态管理", description = "用户动态相关接口")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private AuthService authService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "创建动态", description = "创建新动态")
    public ResponseEntity<Map<String, Object>> createPost(@Valid @RequestPart("postDTO") PostDTO postDTO,
                                                         @RequestPart(name = "files", required = false) List<MultipartFile> files) {
        // 将files设置到postDTO中
        postDTO.setFiles(files);
        Long userId = authService.getCurrentUser().getId();
        Post post = postService.createPost(postDTO, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "创建成功");
        response.put("data", post);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取动态详情", description = "根据动态ID获取动态详情")
    public ResponseEntity<Map<String, Object>> getPostById(@PathVariable(name = "id") Long id) {
        Post post = postService.getPostById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", post);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "更新动态", description = "根据动态ID更新动态")
    public ResponseEntity<Map<String, Object>> updatePost(@PathVariable(name = "id") Long id, 
                                                         @Valid @RequestPart("postDTO") PostDTO postDTO,
                                                         @RequestPart(name = "files", required = false) List<MultipartFile> files) {
        // 将files设置到postDTO中
        postDTO.setFiles(files);
        Long userId = authService.getCurrentUser().getId();
        Post updatedPost = postService.updatePost(id, postDTO, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "更新成功");
        response.put("data", updatedPost);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除动态", description = "根据动态ID删除动态")
    public ResponseEntity<Map<String, Object>> deletePost(@PathVariable(name = "id") Long id) {
        Long userId = authService.getCurrentUser().getId();
        postService.deletePost(id, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/author/{authorId}")
    @Operation(summary = "获取作者的动态列表", description = "根据作者ID获取动态列表")
    public ResponseEntity<Map<String, Object>> getPostsByAuthorId(@PathVariable(name = "authorId") Long authorId) {
        List<Post> posts = postService.getPostsByAuthorId(authorId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", posts);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/timeline")
    @Operation(summary = "获取时间线", description = "获取用户关注的人的动态列表")
    public ResponseEntity<Map<String, Object>> getTimeline(@RequestParam(name = "authorIds", required = false) String authorIds) {
        List<Long> authorIdList = new ArrayList<>();
        if (authorIds != null && !authorIds.isEmpty()) {
            // 处理逗号分隔的字符串
            String[] idStrings = authorIds.split(",");
            for (String idStr : idStrings) {
                authorIdList.add(Long.parseLong(idStr.trim()));
            }
        }
        List<Post> posts = postService.getTimeline(authorIdList);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", posts);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hot")
    @Operation(summary = "获取热门动态", description = "获取热门动态列表")
    public ResponseEntity<Map<String, Object>> getHotPosts() {
        List<Post> posts = postService.getHotPosts();
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", posts);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/like")
    @Operation(summary = "点赞动态", description = "点赞指定动态")
    public ResponseEntity<Map<String, Object>> likePost(@PathVariable(name = "id") Long id) {
        Long userId = authService.getCurrentUser().getId();
        postService.likePost(id, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "点赞成功");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/like")
    @Operation(summary = "取消点赞动态", description = "取消点赞指定动态")
    public ResponseEntity<Map<String, Object>> unlikePost(@PathVariable(name = "id") Long id) {
        Long userId = authService.getCurrentUser().getId();
        postService.unlikePost(id, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "取消点赞成功");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/is-liked")
    @Operation(summary = "检查是否已点赞动态", description = "检查用户是否已点赞指定动态")
    public ResponseEntity<Map<String, Object>> isLiked(@PathVariable(name = "id") Long id) {
        Long userId = authService.getCurrentUser().getId();
        boolean isLiked = postService.isLiked(id, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取成功");
        response.put("data", isLiked);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/repost")
    @Operation(summary = "转发动态", description = "转发指定动态")
    public ResponseEntity<Map<String, Object>> repost(@PathVariable(name = "id") Long id, @RequestParam(name = "content") String content) {
        Long userId = authService.getCurrentUser().getId();
        Post repost = postService.repost(id, content, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "转发成功");
        response.put("data", repost);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}