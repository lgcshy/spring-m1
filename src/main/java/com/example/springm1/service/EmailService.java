package com.example.springm1.service;

/**
 * 邮件服务接口
 */
public interface EmailService {
    
    /**
     * 发送简单文本邮件
     * 
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleEmail(String to, String subject, String content);
    
    /**
     * 发送HTML格式邮件
     * 
     * @param to 收件人
     * @param subject 主题
     * @param content HTML内容
     */
    void sendHtmlEmail(String to, String subject, String content);
    
    /**
     * 发送带附件的邮件
     * 
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @param filePath 附件路径
     * @param fileName 附件名称
     */
    void sendAttachmentEmail(String to, String subject, String content, String filePath, String fileName);
} 