-- 创建数据库
CREATE DATABASE IF NOT EXISTS weibo DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE weibo;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL UNIQUE COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码（加密存储）',
  `email` varchar(100) NOT NULL UNIQUE COMMENT '邮箱',
  `phone` varchar(20) UNIQUE COMMENT '手机号',
  `nickname` varchar(50) NOT NULL COMMENT '昵称',
  `avatar_id` bigint COMMENT '头像文件ID',
  `bio` varchar(200) COMMENT '个人简介',
  `location` varchar(100) COMMENT '所在地',
  `website` varchar(200) COMMENT '个人网站',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  PRIMARY KEY (`id`),
  INDEX `idx_username` (`username`),
  INDEX `idx_email` (`email`),
  INDEX `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 文件表
CREATE TABLE IF NOT EXISTS `file` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `filename` varchar(255) NOT NULL COMMENT '文件名',
  `original_filename` varchar(255) NOT NULL COMMENT '原始文件名',
  `content_type` varchar(100) NOT NULL COMMENT '文件类型',
  `size` bigint NOT NULL COMMENT '文件大小',
  `path` varchar(255) NOT NULL COMMENT '文件存储路径',
  `url` varchar(500) NOT NULL COMMENT '访问URL',
  `uploader_id` bigint NOT NULL COMMENT '上传者ID',
  `post_id` bigint COMMENT '关联的动态ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_uploader_id` (`uploader_id`),
  INDEX `idx_post_id` (`post_id`),
  FOREIGN KEY (`uploader_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件表';

-- 动态表
CREATE TABLE IF NOT EXISTS `post` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL COMMENT '动态内容',
  `author_id` bigint NOT NULL COMMENT '作者ID',
  `parent_id` bigint COMMENT '转发的原动态ID',
  `likes_count` int NOT NULL DEFAULT 0 COMMENT '点赞数',
  `comments_count` int NOT NULL DEFAULT 0 COMMENT '评论数',
  `reposts_count` int NOT NULL DEFAULT 0 COMMENT '转发数',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_author_id` (`author_id`),
  INDEX `idx_parent_id` (`parent_id`),
  INDEX `idx_created_at` (`created_at`),
  FOREIGN KEY (`author_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`parent_id`) REFERENCES `post` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='动态表';

-- 评论表
CREATE TABLE IF NOT EXISTS `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL COMMENT '评论内容',
  `author_id` bigint NOT NULL COMMENT '评论者ID',
  `post_id` bigint NOT NULL COMMENT '动态ID',
  `parent_id` bigint COMMENT '父评论ID',
  `likes_count` int NOT NULL DEFAULT 0 COMMENT '点赞数',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_author_id` (`author_id`),
  INDEX `idx_post_id` (`post_id`),
  INDEX `idx_parent_id` (`parent_id`),
  FOREIGN KEY (`author_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`post_id`) REFERENCES `post` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`parent_id`) REFERENCES `comment` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 点赞表
CREATE TABLE IF NOT EXISTS `like` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '点赞者ID',
  `target_type` tinyint NOT NULL COMMENT '点赞目标类型：1-动态，2-评论',
  `target_id` bigint NOT NULL COMMENT '点赞目标ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_target` (`user_id`, `target_type`, `target_id`),
  INDEX `idx_target` (`target_type`, `target_id`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞表';

-- 关注表
CREATE TABLE IF NOT EXISTS `follow` (
  `follower_id` bigint NOT NULL COMMENT '关注者ID',
  `following_id` bigint NOT NULL COMMENT '被关注者ID',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
  PRIMARY KEY (`follower_id`, `following_id`),
  INDEX `idx_following` (`following_id`),
  FOREIGN KEY (`follower_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`following_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='关注表';

-- 通知表
CREATE TABLE IF NOT EXISTS `notification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '接收通知用户ID',
  `sender_id` bigint NOT NULL COMMENT '发送者ID',
  `type` tinyint NOT NULL COMMENT '通知类型：1-点赞，2-评论，3-转发，4-关注',
  `content` text NOT NULL COMMENT '通知内容',
  `target_id` bigint NOT NULL COMMENT '关联目标ID',
  `is_read` tinyint NOT NULL DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_is_read` (`is_read`),
  INDEX `idx_created_at` (`created_at`),
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`sender_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知表';

-- 插入测试数据
-- 插入测试用户
INSERT INTO `user` (`username`, `password`, `email`, `nickname`) VALUES
('admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 'admin@example.com', '管理员'),
('testuser', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', 'test@example.com', '测试用户');

-- 插入测试动态
INSERT INTO `post` (`content`, `author_id`) VALUES
('这是一条测试动态', 1),
('这是另一条测试动态', 2);

-- 插入测试评论
INSERT INTO `comment` (`content`, `author_id`, `post_id`) VALUES
('这是一条测试评论', 2, 1),
('这是另一条测试评论', 1, 2);

-- 插入测试关注关系
INSERT INTO `follow` (`follower_id`, `following_id`) VALUES
(2, 1);

-- 插入测试点赞
INSERT INTO `like` (`user_id`, `target_type`, `target_id`) VALUES
(2, 1, 1),
(1, 1, 2),
(2, 2, 1);

-- 插入测试通知
INSERT INTO `notification` (`user_id`, `sender_id`, `type`, `content`, `target_id`) VALUES
(1, 2, 1, '点赞了你的动态', 1),
(2, 1, 2, '评论了你的动态', 2),
(1, 2, 4, '关注了你', 2);