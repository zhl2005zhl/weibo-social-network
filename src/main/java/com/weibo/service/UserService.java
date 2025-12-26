package com.weibo.service;

import com.weibo.entity.User;
import com.weibo.dto.RegisterDTO;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 根据ID获取用户信息
     */
    User getUserById(Long id);

    /**
     * 根据用户名获取用户信息
     */
    User getUserByUsername(String username);

    /**
     * 更新用户信息
     */
    User updateUser(Long id, User user);

    /**
     * 更新用户头像
     */
    User updateAvatar(Long userId, Long avatarId);

    /**
     * 删除用户
     */
    void deleteUser(Long id);

    /**
     * 搜索用户
     */
    List<User> searchUsers(String keyword);

    /**
     * 获取用户的关注数
     */
    Long getFollowingCount(Long userId);

    /**
     * 获取用户的粉丝数
     */
    Long getFollowersCount(Long userId);

    /**
     * 检查用户是否已关注
     */
    boolean isFollowing(Long followerId, Long followingId);
}
