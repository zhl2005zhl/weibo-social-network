package com.weibo.repository;

import com.weibo.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 评论数据访问层
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 根据动态ID查询评论列表
     */
    List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId);

    /**
     * 根据父评论ID查询子评论列表
     */
    List<Comment> findByParentIdOrderByCreatedAtAsc(Long parentId);

    /**
     * 根据作者ID查询评论列表
     */
    List<Comment> findByAuthorIdOrderByCreatedAtDesc(Long authorId);
}
