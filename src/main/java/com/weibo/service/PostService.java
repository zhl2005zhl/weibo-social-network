package com.weibo.service;

import com.weibo.dto.PostDTO;
import com.weibo.entity.Post;

import java.util.List;

/**
 * 动态服务接口
 */
public interface PostService {

    /**
     * 创建动态
     */
    Post createPost(PostDTO postDTO, Long userId);

    /**
     * 根据ID获取动态
     */
    Post getPostById(Long id);

    /**
     * 更新动态
     */
    Post updatePost(Long id, PostDTO postDTO, Long userId);

    /**
     * 删除动态
     */
    void deletePost(Long id, Long userId);

    /**
     * 根据作者ID获取动态列表
     */
    List<Post> getPostsByAuthorId(Long authorId);

    /**
     * 获取用户时间线（关注的人的动态）
     */
    List<Post> getTimeline(List<Long> authorIds);

    /**
     * 获取热门动态
     */
    List<Post> getHotPosts();

    /**
     * 点赞动态
     */
    void likePost(Long postId, Long userId);

    /**
     * 取消点赞动态
     */
    void unlikePost(Long postId, Long userId);

    /**
     * 检查用户是否已点赞动态
     */
    boolean isLiked(Long postId, Long userId);

    /**
     * 转发动态
     */
    Post repost(Long postId, String content, Long userId);
}
