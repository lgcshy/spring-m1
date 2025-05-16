package com.example.springm1.service.impl;

import com.example.springm1.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * 邮件服务实现类
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendSimpleEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        
        try {
            mailSender.send(message);
            logger.info("简单邮件已发送。");
        } catch (Exception e) {
            logger.error("发送简单邮件时发生错误: {}", e.getMessage());
            throw new RuntimeException("邮件发送失败", e);
        }
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            
            mailSender.send(message);
            logger.info("HTML邮件已发送。");
        } catch (MessagingException e) {
            logger.error("发送HTML邮件时发生错误: {}", e.getMessage());
            throw new RuntimeException("邮件发送失败", e);
        }
    }

    @Override
    public void sendAttachmentEmail(String to, String subject, String content, String filePath, String fileName) {
        MimeMessage message = mailSender.createMimeMessage();
        
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
            
            FileSystemResource file = new FileSystemResource(new File(filePath));
            helper.addAttachment(fileName, file);
            
            mailSender.send(message);
            logger.info("带附件的邮件已发送。");
        } catch (MessagingException e) {
            logger.error("发送带附件的邮件时发生错误: {}", e.getMessage());
            throw new RuntimeException("邮件发送失败", e);
        }
    }
} 