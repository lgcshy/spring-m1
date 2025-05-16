package com.example.springm1.controller;

import com.example.springm1.common.ApiResponse;
import com.example.springm1.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件上传控制器
 */
@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * 上传单个文件
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "directory", required = false) String directory) {
        
        try {
            String fileUrl = fileStorageService.uploadFile(file, directory);
            
            Map<String, Object> data = new HashMap<>();
            data.put("url", fileUrl);
            data.put("filename", file.getOriginalFilename());
            data.put("size", file.getSize());
            data.put("contentType", file.getContentType());
            
            return ResponseEntity.ok(ApiResponse.success(data, "文件上传成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("文件上传失败: " + e.getMessage()));
        }
    }

    /**
     * 上传多个文件
     */
    @PostMapping("/upload/multiple")
    public ResponseEntity<Map<String, Object>> uploadMultipleFiles(
            @RequestParam("files") MultipartFile[] files,
            @RequestParam(value = "directory", required = false) String directory) {
        
        try {
            Map<String, String> uploadedFiles = new HashMap<>();
            
            for (MultipartFile file : files) {
                String fileUrl = fileStorageService.uploadFile(file, directory);
                uploadedFiles.put(file.getOriginalFilename(), fileUrl);
            }
            
            Map<String, Object> data = new HashMap<>();
            data.put("uploadedFiles", uploadedFiles);
            data.put("count", uploadedFiles.size());
            
            return ResponseEntity.ok(ApiResponse.success(data, "文件上传成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("文件上传失败: " + e.getMessage()));
        }
    }

    /**
     * 删除文件
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteFile(@RequestParam("fileUrl") String fileUrl) {
        boolean deleted = fileStorageService.deleteFile(fileUrl);
        
        if (deleted) {
            return ResponseEntity.ok(ApiResponse.successWithMessage("文件删除成功"));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("文件删除失败"));
        }
    }

    /**
     * 获取文件临时访问URL
     */
    @GetMapping("/url")
    public ResponseEntity<Map<String, Object>> getFileUrl(
            @RequestParam("objectName") String objectName,
            @RequestParam(value = "expirySeconds", defaultValue = "3600") int expirySeconds) {
        
        try {
            String presignedUrl = fileStorageService.getPresignedUrl(objectName, expirySeconds);
            
            Map<String, Object> data = new HashMap<>();
            data.put("url", presignedUrl);
            data.put("expirySeconds", expirySeconds);
            
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取文件URL失败: " + e.getMessage()));
        }
    }

    /**
     * 列出文件
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> listFiles(
            @RequestParam(value = "directory", required = false) String directory) {
        
        try {
            List<Map<String, Object>> files = fileStorageService.listFiles(directory);
            
            Map<String, Object> data = new HashMap<>();
            data.put("files", files);
            data.put("count", files.size());
            data.put("directory", directory == null ? "" : directory);
            
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("列出文件失败: " + e.getMessage()));
        }
    }
} 