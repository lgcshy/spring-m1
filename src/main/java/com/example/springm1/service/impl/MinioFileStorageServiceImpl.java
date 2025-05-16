package com.example.springm1.service.impl;

import com.example.springm1.service.FileStorageService;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * MinIO文件存储服务实现类
 */
@Service
public class MinioFileStorageServiceImpl implements FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(MinioFileStorageServiceImpl.class);

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String endpoint;

    @Override
    public String uploadFile(MultipartFile file, String directory) {
        try {
            String originalFilename = file.getOriginalFilename();
            String fileName = generateUniqueFileName(originalFilename);
            String objectName = buildObjectName(directory, fileName);
            
            // 上传文件
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            
            logger.info("文件上传成功: {}", objectName);
            return endpoint + "/" + bucketName + "/" + objectName;
        } catch (Exception e) {
            logger.error("文件上传失败: {}", e.getMessage());
            throw new RuntimeException("文件上传失败", e);
        }
    }

    @Override
    public String uploadFile(InputStream inputStream, String fileName, String contentType, String directory) {
        try {
            String uniqueFileName = generateUniqueFileName(fileName);
            String objectName = buildObjectName(directory, uniqueFileName);
            
            // 上传文件
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, -1, 10485760) // 10MB buffer
                            .contentType(contentType)
                            .build()
            );
            
            logger.info("文件上传成功: {}", objectName);
            return endpoint + "/" + bucketName + "/" + objectName;
        } catch (Exception e) {
            logger.error("文件上传失败: {}", e.getMessage());
            throw new RuntimeException("文件上传失败", e);
        }
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        try {
            String objectName = extractObjectName(fileUrl);
            
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            
            logger.info("文件删除成功: {}", objectName);
            return true;
        } catch (Exception e) {
            logger.error("文件删除失败: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String getPresignedUrl(String objectName, int expirySeconds) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(expirySeconds, TimeUnit.SECONDS)
                            .build()
            );
        } catch (Exception e) {
            logger.error("获取文件URL失败: {}", e.getMessage());
            throw new RuntimeException("获取文件URL失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> listFiles(String directory) {
        List<Map<String, Object>> fileList = new ArrayList<>();
        try {
            // 确保目录以/结尾
            if (StringUtils.hasText(directory) && !directory.endsWith("/")) {
                directory += "/";
            }
            
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(bucketName)
                            .prefix(directory)
                            .recursive(false)
                            .build()
            );
            
            for (Result<Item> result : results) {
                Item item = result.get();
                Map<String, Object> fileInfo = new HashMap<>();
                fileInfo.put("name", item.objectName());
                fileInfo.put("size", item.size());
                fileInfo.put("lastModified", item.lastModified());
                fileInfo.put("isDirectory", item.isDir());
                fileInfo.put("url", endpoint + "/" + bucketName + "/" + item.objectName());
                
                fileList.add(fileInfo);
            }
        } catch (Exception e) {
            logger.error("列出文件失败: {}", e.getMessage());
            throw new RuntimeException("列出文件失败", e);
        }
        
        return fileList;
    }

    /**
     * 生成唯一文件名
     */
    private String generateUniqueFileName(String originalFilename) {
        if (originalFilename == null) {
            return UUID.randomUUID().toString();
        }
        
        String extension = "";
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex > 0) {
            extension = originalFilename.substring(lastDotIndex);
        }
        
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        
        return timestamp + "_" + uuid + extension;
    }

    /**
     * 构建对象名称
     */
    private String buildObjectName(String directory, String fileName) {
        if (!StringUtils.hasText(directory)) {
            return fileName;
        }
        
        // 确保目录以/结尾
        if (!directory.endsWith("/")) {
            directory += "/";
        }
        
        return directory + fileName;
    }

    /**
     * 从URL中提取对象名称
     */
    private String extractObjectName(String fileUrl) {
        if (fileUrl.startsWith(endpoint + "/" + bucketName + "/")) {
            return fileUrl.substring((endpoint + "/" + bucketName + "/").length());
        }
        return fileUrl;
    }
} 