package com.weibo.service;

import com.weibo.dto.LoginDTO;
import com.weibo.dto.RegisterDTO;
import com.weibo.entity.User;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     */
    String login(LoginDTO loginDTO);

    /**
     * 用户注册
     */
    void register(RegisterDTO registerDTO);

    /**
     * 获取当前登录用户
     */
    User getCurrentUser();

    /**
     * 刷新Token
     */
    String refreshToken(String oldToken);
}