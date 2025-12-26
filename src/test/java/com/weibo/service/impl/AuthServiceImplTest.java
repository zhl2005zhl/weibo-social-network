package com.weibo.service.impl;

import com.weibo.dto.RegisterDTO;
import com.weibo.entity.User;
import com.weibo.enums.UserStatus;
import com.weibo.repository.UserRepository;
import com.weibo.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * AuthServiceImpl 单元测试
 */
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 测试用户注册功能
     */
    @Test
    void testRegister() {
        // 准备测试数据
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("testuser");
        registerDTO.setPassword("password123");
        registerDTO.setEmail("test@example.com");
        registerDTO.setNickname("测试用户");

        // 模拟依赖行为
        when(userRepository.existsByUsername(registerDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registerDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerDTO.getPassword())).thenReturn("encrypted_password");

        // 执行测试
        authService.register(registerDTO);

        // 验证结果
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * 测试注册已存在的用户名
     */
    @Test
    void testRegisterWithExistingUsername() {
        // 准备测试数据
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("existinguser");
        registerDTO.setPassword("password123");
        registerDTO.setEmail("test@example.com");
        registerDTO.setNickname("测试用户");

        // 模拟依赖行为
        when(userRepository.existsByUsername(registerDTO.getUsername())).thenReturn(true);

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> authService.register(registerDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    /**
     * 测试注册已存在的邮箱
     */
    @Test
    void testRegisterWithExistingEmail() {
        // 准备测试数据
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("testuser");
        registerDTO.setPassword("password123");
        registerDTO.setEmail("existing@example.com");
        registerDTO.setNickname("测试用户");

        // 模拟依赖行为
        when(userRepository.existsByUsername(registerDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registerDTO.getEmail())).thenReturn(true);

        // 执行测试并验证异常
        assertThrows(RuntimeException.class, () -> authService.register(registerDTO));
        verify(userRepository, never()).save(any(User.class));
    }
}
