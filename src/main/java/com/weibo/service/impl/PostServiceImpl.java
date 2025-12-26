package com.weibo.service.impl;

import com.weibo.dto.PostDTO;
import com.weibo.entity.Post;
import com.weibo.enums.LikeType;
import com.weibo.enums.NotificationType;
import com.weibo.repository.PostRepository;
import com.weibo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 动态服务实现类
 */
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FileService fileService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private NotificationService notificationService;

    @Override
    @Transactional
    public Post createPost(PostDTO postDTO, Long userId) {
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setAuthorId(userId);
        post.setParentId(postDTO.getParentId());
        
        Post savedPost = postRepository.save(post);

        // 处理图片上传
        if (postDTO.getFiles() != null && !postDTO.getFiles().isEmpty()) {
            for (var file : postDTO.getFiles()) {
                fileService.uploadFile(file, userId, savedPost.getId());
            }
        }

        return savedPost;
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("动态不存在"));
    }

    @Override
    @Transactional
    public Post updatePost(Long id, PostDTO postDTO, Long userId) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("动态不存在"));

        // 检查是否是作者本人
        if (!existingPost.getAuthorId().equals(userId)) {
            throw new RuntimeException("无权限修改此动态");
        }

        // 更新动态内容
        existingPost.setContent(postDTO.getContent());
        
        return postRepository.save(existingPost);
    }

    @Override
    @Transactional
    public void deletePost(Long id, Long userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("动态不存在"));

        // 检查是否是作者本人
        if (!post.getAuthorId().equals(userId)) {
            throw new RuntimeException("无权限删除此动态");
        }

        postRepository.delete(post);
    }

    @Override
    public List<Post> getPostsByAuthorId(Long authorId) {
        return postRepository.findByAuthorIdOrderByCreatedAtDesc(authorId);
    }

    @Override
    public List<Post> getTimeline(List<Long> authorIds) {
        // 当authorIds为空时，返回空列表，避免SQL语法错误
        if (authorIds == null || authorIds.isEmpty()) {
            return List.of();
        }
        return postRepository.findByAuthorIdInOrderByCreatedAtDesc(authorIds);
    }

    @Override
    public List<Post> getHotPosts() {
        return postRepository.findHotPosts();
    }

    @Override
    @Transactional
    public void likePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("动态不存在"));

        // 检查是否已点赞
        if (likeService.isLiked(userId, LikeType.POST.getValue(), postId)) {
            throw new RuntimeException("已经点赞过此动态");
        }

        // 添加点赞记录
        likeService.addLike(userId, LikeType.POST.getValue(), postId);

        // 更新动态点赞数
        post.setLikesCount(post.getLikesCount() + 1);
        postRepository.save(post);

        // 发送点赞通知（如果点赞的不是自己的动态）
        if (!post.getAuthorId().equals(userId)) {
            notificationService.sendNotification(
                    post.getAuthorId(),
                    userId,
                    NotificationType.LIKE.getValue(),
                    "点赞了你的动态",
                    postId
            );
        }
    }

    @Override
    @Transactional
    public void unlikePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("动态不存在"));

        // 检查是否已点赞
        if (!likeService.isLiked(userId, LikeType.POST.getValue(), postId)) {
            throw new RuntimeException("还没有点赞此动态");
        }

        // 删除点赞记录
        likeService.removeLike(userId, LikeType.POST.getValue(), postId);

        // 更新动态点赞数
        post.setLikesCount(Math.max(0, post.getLikesCount() - 1));
        postRepository.save(post);
    }

    @Override
    public boolean isLiked(Long postId, Long userId) {
        return likeService.isLiked(userId, LikeType.POST.getValue(), postId);
    }

    @Override
    @Transactional
    public Post repost(Long postId, String content, Long userId) {
        Post originalPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("动态不存在"));

        // 创建转发动态
        Post repost = new Post();
        repost.setContent(content);
        repost.setAuthorId(userId);
        repost.setParentId(postId);
        
        Post savedRepost = postRepository.save(repost);

        // 更新原动态转发数
        originalPost.setRepostsCount(originalPost.getRepostsCount() + 1);
        postRepository.save(originalPost);

        // 发送转发通知（如果转发的不是自己的动态）
        if (!originalPost.getAuthorId().equals(userId)) {
            notificationService.sendNotification(
                    originalPost.getAuthorId(),
                    userId,
                    NotificationType.REPOST.getValue(),
                    "转发了你的动态",
                    postId
            );
        }

        return savedRepost;
    }
}
