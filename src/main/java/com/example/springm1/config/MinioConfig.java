package com.example.springm1.config;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MinIO配置类
 */
@Configuration
public class MinioConfig {

    private static final Logger logger = LoggerFactory.getLogger(MinioConfig.class);

    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucket}")
    private String bucket;

    /**
     * 初始化MinioClient
     */
    @Bean
    public MinioClient minioClient() {
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
            
            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!isExist) {
                // 创建存储桶
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
                logger.info("创建存储桶成功: {}", bucket);
            }
            
            return minioClient;
        } catch (Exception e) {
            logger.error("初始化MinIO客户端失败: {}", e.getMessage());
            throw new RuntimeException("初始化MinIO客户端失败", e);
        }
    }
} 