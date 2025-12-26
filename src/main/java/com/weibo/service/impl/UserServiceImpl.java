package com.weibo.service.impl;

import com.weibo.entity.User;
import com.weibo.repository.UserRepository;
import com.weibo.service.UserService;
import com.weibo.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务实现类（添加缓存支持）
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Override
    @Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    @Override
    @Cacheable(value = "users", key = "#username")
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    @Override
    @CachePut(value = "users", key = "#id")
    public User updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // 更新用户信息
        if (user.getNickname() != null) {
            existingUser.setNickname(user.getNickname());
        }
        if (user.getBio() != null) {
            existingUser.setBio(user.getBio());
        }
        if (user.getLocation() != null) {
            existingUser.setLocation(user.getLocation());
        }
        if (user.getWebsite() != null) {
            existingUser.setWebsite(user.getWebsite());
        }
        if (user.getPhone() != null) {
            existingUser.setPhone(user.getPhone());
        }

        return userRepository.save(existingUser);
    }

    @Override
    @CachePut(value = "users", key = "#userId")
    public User updateAvatar(Long userId, Long avatarId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        user.setAvatarId(avatarId);
        return userRepository.save(user);
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> searchUsers(String keyword) {
        // 搜索结果不缓存，每次都从数据库查询最新数据
        return userRepository.findByUsernameContainingOrNicknameContaining(keyword, keyword);
    }

    @Override
    @Cacheable(value = "followCounts", key = "#userId + '_following'")
    public Long getFollowingCount(Long userId) {
        return followRepository.countByIdFollowerId(userId);
    }

    @Override
    @Cacheable(value = "followCounts", key = "#userId + '_followers'")
    public Long getFollowersCount(Long userId) {
        return followRepository.countByIdFollowingId(userId);
    }

    @Override
    @Cacheable(value = "followStatus", key = "#followerId + '_' + #followingId")
    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsByIdFollowerIdAndIdFollowingId(followerId, followingId);
    }
}