package com.weibo.repository;

import com.weibo.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 动态数据访问层
 */
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 根据作者ID查询动态列表
     */
    List<Post> findByAuthorIdOrderByCreatedAtDesc(Long authorId);

    /**
     * 根据作者ID列表查询动态列表（用于时间线）
     */
    @Query("SELECT p FROM Post p WHERE p.authorId IN :authorIds ORDER BY p.createdAt DESC")
    List<Post> findByAuthorIdInOrderByCreatedAtDesc(@Param("authorIds") List<Long> authorIds);

    /**
     * 查询热门动态（根据点赞数、评论数、转发数综合排序）
     */
    @Query("SELECT p FROM Post p ORDER BY (p.likesCount + p.commentsCount + p.repostsCount) DESC")
    List<Post> findHotPosts();

    /**
     * 查询转发的原动态
     */
    List<Post> findByParentId(Long parentId);
}