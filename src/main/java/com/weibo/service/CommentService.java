package com.weibo.service;

import com.weibo.dto.CommentDTO;
import com.weibo.entity.Comment;

import java.util.List;

/**
 * 评论服务接口
 */
public interface CommentService {

    /**
     * 创建评论
     */
    Comment createComment(CommentDTO commentDTO, Long postId, Long userId);

    /**
     * 根据ID获取评论
     */
    Comment getCommentById(Long id);

    /**
     * 更新评论
     */
    Comment updateComment(Long id, CommentDTO commentDTO, Long userId);

    /**
     * 删除评论
     */
    void deleteComment(Long id, Long userId);

    /**
     * 根据动态ID获取评论列表
     */
    List<Comment> getCommentsByPostId(Long postId);

    /**
     * 点赞评论
     */
    void likeComment(Long commentId, Long userId);

    /**
     * 取消点赞评论
     */
    void unlikeComment(Long commentId, Long userId);

    /**
     * 检查用户是否已点赞评论
     */
    boolean isLiked(Long commentId, Long userId);
}
