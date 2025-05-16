package com.example.springm1;

import com.example.springm1.service.EmailService;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class EmailServiceTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("test@example.com", "test"));

    @Autowired
    private EmailService emailService;

    @Test
    public void testSendSimpleEmail() throws MessagingException {
        String testEmail = "recipient@example.com";
        String subject = "测试简单邮件";
        String content = "这是一封测试邮件内容";
        
        emailService.sendSimpleEmail(testEmail, subject, content);
        
        // 验证邮件是否发送成功
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        assertEquals(subject, receivedMessages[0].getSubject());
        
        // 验证收件人
        assertEquals(testEmail, receivedMessages[0].getAllRecipients()[0].toString());
        
        // 邮件测试通过
        assertTrue(true);
    }

    @Test
    public void testSendHtmlEmail() throws MessagingException {
        String testEmail = "recipient@example.com";
        String subject = "测试HTML邮件";
        String htmlContent = "<h1>测试HTML邮件</h1><p>这是一封<b>HTML格式</b>的测试邮件</p>";
        
        emailService.sendHtmlEmail(testEmail, subject, htmlContent);
        
        // 验证邮件是否发送成功
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertEquals(1, receivedMessages.length);
        assertEquals(subject, receivedMessages[0].getSubject());
        
        // 验证收件人
        assertEquals(testEmail, receivedMessages[0].getAllRecipients()[0].toString());
        
        // 邮件测试通过
        assertTrue(true);
    }
} 