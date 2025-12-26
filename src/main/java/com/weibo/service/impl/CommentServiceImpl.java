package com.weibo.service.impl;

import com.weibo.dto.CommentDTO;
import com.weibo.entity.Comment;
import com.weibo.entity.Post;
import com.weibo.enums.LikeType;
import com.weibo.enums.NotificationType;
import com.weibo.repository.CommentRepository;
import com.weibo.repository.PostRepository;
import com.weibo.service.CommentService;
import com.weibo.service.LikeService;
import com.weibo.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 评论服务实现类
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeService likeService;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public Comment createComment(CommentDTO commentDTO, Long postId, Long userId) {
        // 检查动态是否存在
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("动态不存在"));

        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setAuthorId(userId);
        comment.setPostId(postId);
        comment.setParentId(commentDTO.getParentId());
        
        Comment savedComment = commentRepository.save(comment);

        // 更新动态评论数
        post.setCommentsCount(post.getCommentsCount() + 1);
        postRepository.save(post);

        // 发送评论通知（如果评论的不是自己的动态）
        if (!post.getAuthorId().equals(userId)) {
            notificationService.sendNotification(
                    post.getAuthorId(),
                    userId,
                    NotificationType.COMMENT.getValue(),
                    "评论了你的动态",
                    postId
            );
        }

        // 如果是回复评论，给被回复的人发送通知
        if (commentDTO.getParentId() != null) {
            Comment parentComment = commentRepository.findById(commentDTO.getParentId())
                    .orElseThrow(() -> new RuntimeException("父评论不存在"));
            
            if (!parentComment.getAuthorId().equals(userId) && !parentComment.getAuthorId().equals(post.getAuthorId())) {
                notificationService.sendNotification(
                        parentComment.getAuthorId(),
                        userId,
                        NotificationType.COMMENT.getValue(),
                        "回复了你的评论",
                        postId
                );
            }
        }

        return savedComment;
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("评论不存在"));
    }

    @Override
    @Transactional
    public Comment updateComment(Long id, CommentDTO commentDTO, Long userId) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("评论不存在"));

        // 检查是否是评论作者本人
        if (!existingComment.getAuthorId().equals(userId)) {
            throw new RuntimeException("无权限修改此评论");
        }

        // 更新评论内容
        existingComment.setContent(commentDTO.getContent());
        
        return commentRepository.save(existingComment);
    }

    @Override
    @Transactional
    public void deleteComment(Long id, Long userId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("评论不存在"));

        // 检查是否是评论作者本人或动态作者
        Post post = postRepository.findById(comment.getPostId())
                .orElseThrow(() -> new RuntimeException("动态不存在"));
        
        if (!comment.getAuthorId().equals(userId) && !post.getAuthorId().equals(userId)) {
            throw new RuntimeException("无权限删除此评论");
        }

        // 更新动态评论数
        post.setCommentsCount(Math.max(0, post.getCommentsCount() - 1));
        postRepository.save(post);

        // 删除评论
        commentRepository.delete(comment);
    }

    @Override
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId);
    }

    @Override
    @Transactional
    public void likeComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("评论不存在"));

        // 检查是否已点赞
        if (likeService.isLiked(userId, LikeType.COMMENT.getValue(), commentId)) {
            throw new RuntimeException("已经点赞过此评论");
        }

        // 添加点赞记录
        likeService.addLike(userId, LikeType.COMMENT.getValue(), commentId);

        // 更新评论点赞数
        comment.setLikesCount(comment.getLikesCount() + 1);
        commentRepository.save(comment);

        // 发送点赞通知（如果点赞的不是自己的评论）
        if (!comment.getAuthorId().equals(userId)) {
            notificationService.sendNotification(
                    comment.getAuthorId(),
                    userId,
                    NotificationType.LIKE.getValue(),
                    "点赞了你的评论",
                    comment.getPostId()
            );
        }
    }

    @Override
    @Transactional
    public void unlikeComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("评论不存在"));

        // 检查是否已点赞
        if (!likeService.isLiked(userId, LikeType.COMMENT.getValue(), commentId)) {
            throw new RuntimeException("还没有点赞此评论");
        }

        // 删除点赞记录
        likeService.removeLike(userId, LikeType.COMMENT.getValue(), commentId);

        // 更新评论点赞数
        comment.setLikesCount(Math.max(0, comment.getLikesCount() - 1));
        commentRepository.save(comment);
    }

    @Override
    public boolean isLiked(Long commentId, Long userId) {
        return likeService.isLiked(userId, LikeType.COMMENT.getValue(), commentId);
    }
}
