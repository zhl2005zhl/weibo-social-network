package com.weibo.repository;

import com.weibo.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 文件数据访问层
 */
public interface FileRepository extends JpaRepository<File, Long> {

    /**
     * 根据上传者ID查询文件列表
     */
    List<File> findByUploaderIdOrderByCreatedAtDesc(Long uploaderId);

    /**
     * 根据动态ID查询文件列表
     */
    List<File> findByPostId(Long postId);

    /**
     * 根据文件名查询文件
     */
    File findByFilename(String filename);
}