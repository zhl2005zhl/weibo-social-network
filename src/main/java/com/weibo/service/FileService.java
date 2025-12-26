package com.weibo.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件服务接口
 */
public interface FileService {

    /**
     * 上传文件
     */
    String uploadFile(MultipartFile file, Long userId, Long postId);

    /**
     * 删除文件
     */
    void deleteFile(Long fileId, Long userId);

    /**
     * 根据文件ID获取文件URL
     */
    String getFileUrl(Long fileId);
}
