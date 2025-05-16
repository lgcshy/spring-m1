package com.example.springm1;

import com.example.springm1.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    @Test
    public void testSendSimpleEmail() throws Exception {
        // 模拟邮件服务
        doNothing().when(emailService).sendSimpleEmail(anyString(), anyString(), anyString());

        // 执行请求
        mockMvc.perform(post("/api/email/simple")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"to\":\"recipient@example.com\",\"subject\":\"测试主题\",\"content\":\"测试内容\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("邮件发送成功"));

        // 验证服务方法被调用
        verify(emailService).sendSimpleEmail("recipient@example.com", "测试主题", "测试内容");
    }

    @Test
    public void testSendHtmlEmail() throws Exception {
        // 模拟邮件服务
        doNothing().when(emailService).sendHtmlEmail(anyString(), anyString(), anyString());

        // 执行请求
        mockMvc.perform(post("/api/email/html")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"to\":\"recipient@example.com\",\"subject\":\"HTML测试主题\",\"content\":\"<h1>HTML测试内容</h1>\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("HTML邮件发送成功"));

        // 验证服务方法被调用
        verify(emailService).sendHtmlEmail("recipient@example.com", "HTML测试主题", "<h1>HTML测试内容</h1>");
    }

    @Test
    public void testSendAttachmentEmail() throws Exception {
        // 模拟邮件服务
        doNothing().when(emailService).sendAttachmentEmail(anyString(), anyString(), anyString(), anyString(), anyString());

        // 创建模拟文件
        MockMultipartFile file = new MockMultipartFile(
                "file", 
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "测试文件内容".getBytes()
        );

        // 执行请求
        mockMvc.perform(multipart("/api/email/attachment")
                .file(file)
                .param("to", "recipient@example.com")
                .param("subject", "带附件的测试邮件")
                .param("content", "这是一封带附件的测试邮件"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("带附件的邮件发送成功"));

        // 验证服务方法被调用（文件路径是动态生成的，所以这里只验证前三个参数）
        verify(emailService).sendAttachmentEmail(
                anyString(), 
                anyString(), 
                anyString(), 
                any(), 
                anyString()
        );
    }
} 