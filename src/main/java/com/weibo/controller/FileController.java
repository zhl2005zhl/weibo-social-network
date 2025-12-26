package com.weibo.controller;

import com.weibo.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件控制器
 */
@RestController
@RequestMapping("/files")
@Tag(name = "文件管理", description = "文件上传下载相关接口")
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传文件")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        // 这里需要获取当前登录用户ID，暂时使用固定值1
        Long userId = 1L;
        String fileUrl = fileService.uploadFile(file, userId, null);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "上传成功");
        response.put("data", fileUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{filename:.+}")
    @Operation(summary = "下载文件", description = "根据文件名下载文件")
    public ResponseEntity<Resource> downloadFile(@PathVariable(name = "filename") String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                throw new RuntimeException("文件不存在");
            }

            String contentType = "application/octet-stream";
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("文件下载失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文件", description = "根据文件ID删除文件")
    public ResponseEntity<Map<String, Object>> deleteFile(@PathVariable(name = "id") Long id) {
        // 这里需要获取当前登录用户ID，暂时使用固定值1
        Long userId = 1L;
        fileService.deleteFile(id, userId);
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "删除成功");
        return ResponseEntity.ok(response);
    }
}