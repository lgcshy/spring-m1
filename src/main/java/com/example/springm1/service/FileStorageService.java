package com.example.springm1.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 文件存储服务接口
 */
public interface FileStorageService {

    /**
     * 上传文件
     *
     * @param file 文件
     * @param directory 目录（可选）
     * @return 文件访问URL
     */
    String uploadFile(MultipartFile file, String directory);

    /**
     * 上传文件
     *
     * @param inputStream 文件输入流
     * @param fileName 文件名
     * @param contentType 内容类型
     * @param directory 目录（可选）
     * @return 文件访问URL
     */
    String uploadFile(InputStream inputStream, String fileName, String contentType, String directory);

    /**
     * 删除文件
     *
     * @param fileUrl 文件URL或对象名称
     * @return 是否删除成功
     */
    boolean deleteFile(String fileUrl);

    /**
     * 获取文件URL
     *
     * @param objectName 对象名称
     * @param expirySeconds 过期时间（秒）
     * @return 临时访问URL
     */
    String getPresignedUrl(String objectName, int expirySeconds);
    
    /**
     * 列出指定目录下的文件
     *
     * @param directory 目录
     * @return 文件列表
     */
    List<Map<String, Object>> listFiles(String directory);
} 