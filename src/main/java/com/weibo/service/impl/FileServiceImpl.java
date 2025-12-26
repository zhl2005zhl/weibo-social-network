package com.weibo.service.impl;

import com.weibo.entity.File;
import com.weibo.repository.FileRepository;
import com.weibo.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * 文件服务实现类
 */
@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private FileRepository fileRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.base-url}")
    private String baseUrl;

    @Override
    @Transactional
    public String uploadFile(MultipartFile file, Long userId, Long postId) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                throw new RuntimeException("文件为空");
            }

            // 检查文件大小
            if (file.getSize() > 10 * 1024 * 1024) { // 10MB
                throw new RuntimeException("文件大小不能超过10MB");
            }

            // 创建存储目录
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String filename = UUID.randomUUID() + fileExtension;

            // 保存文件到磁盘
            Path filePath = uploadPath.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // 保存文件信息到数据库
            File fileEntity = new File();
            fileEntity.setFilename(filename);
            fileEntity.setOriginalFilename(originalFilename);
            fileEntity.setContentType(file.getContentType());
            fileEntity.setSize(file.getSize());
            fileEntity.setPath(filePath.toString());
            fileEntity.setUrl(baseUrl + "/" + filename);
            fileEntity.setUploaderId(userId);
            fileEntity.setPostId(postId);
            fileRepository.save(fileEntity);

            return fileEntity.getUrl();
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteFile(Long fileId, Long userId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("文件不存在"));
        
        // 检查是否是该用户上传的文件
        if (!file.getUploaderId().equals(userId)) {
            throw new RuntimeException("无权限删除此文件");
        }
        
        try {
            // 删除磁盘上的文件
            Path filePath = Paths.get(file.getPath());
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
            
            // 删除数据库中的文件记录
            fileRepository.delete(file);
        } catch (IOException e) {
            throw new RuntimeException("文件删除失败: " + e.getMessage());
        }
    }

    @Override
    public String getFileUrl(Long fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("文件不存在"));
        return file.getUrl();
    }
}
