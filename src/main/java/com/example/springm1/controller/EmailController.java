package com.example.springm1.controller;

import com.example.springm1.common.ApiResponse;
import com.example.springm1.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

/**
 * 邮件控制器
 */
@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    /**
     * 发送简单文本邮件
     */
    @PostMapping("/simple")
    public ResponseEntity<Map<String, Object>> sendSimpleEmail(@RequestBody Map<String, String> emailRequest) {
        String to = emailRequest.get("to");
        String subject = emailRequest.get("subject");
        String content = emailRequest.get("content");
        
        emailService.sendSimpleEmail(to, subject, content);
        
        return ResponseEntity.ok(ApiResponse.successWithMessage("邮件发送成功"));
    }

    /**
     * 发送HTML格式邮件
     */
    @PostMapping("/html")
    public ResponseEntity<Map<String, Object>> sendHtmlEmail(@RequestBody Map<String, String> emailRequest) {
        String to = emailRequest.get("to");
        String subject = emailRequest.get("subject");
        String content = emailRequest.get("content");
        
        emailService.sendHtmlEmail(to, subject, content);
        
        return ResponseEntity.ok(ApiResponse.successWithMessage("HTML邮件发送成功"));
    }

    /**
     * 发送带附件的邮件
     */
    @PostMapping("/attachment")
    public ResponseEntity<Map<String, Object>> sendAttachmentEmail(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("content") String content,
            @RequestParam("file") MultipartFile file) {
        
        try {
            // 创建临时文件
            String fileName = file.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
            
            // 确保临时目录存在
            Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "email-attachments");
            if (!Files.exists(tempDir)) {
                Files.createDirectories(tempDir);
            }
            
            // 保存文件到临时目录
            Path filePath = tempDir.resolve(uniqueFileName);
            file.transferTo(filePath.toFile());
            
            // 发送邮件
            emailService.sendAttachmentEmail(to, subject, content, filePath.toString(), fileName);
            
            // 发送后删除临时文件
            Files.deleteIfExists(filePath);
            
            return ResponseEntity.ok(ApiResponse.successWithMessage("带附件的邮件发送成功"));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("附件处理失败: " + e.getMessage()));
        }
    }
} 