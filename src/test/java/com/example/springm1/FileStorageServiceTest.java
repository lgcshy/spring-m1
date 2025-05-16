package com.example.springm1;

import com.example.springm1.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class FileStorageServiceTest {

    @Autowired
    private FileStorageService fileStorageService;

    @TempDir
    Path tempDir;

    @Test
    public void testUploadFile() throws IOException {
        // 创建临时测试文件
        Path tempFile = tempDir.resolve("test.txt");
        Files.write(tempFile.toFile().toPath(), "测试文件内容".getBytes());

        // 创建MockMultipartFile
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                new FileInputStream(tempFile.toFile())
        );

        // 上传文件
        String fileUrl = fileStorageService.uploadFile(multipartFile, "test");
        
        // 验证文件URL不为空
        assertNotNull(fileUrl);
        assertTrue(fileUrl.contains("/test/"));
        
        // 列出文件
        List<Map<String, Object>> files = fileStorageService.listFiles("test");
        assertFalse(files.isEmpty());
        
        // 删除文件
        boolean deleted = fileStorageService.deleteFile(fileUrl);
        assertTrue(deleted);
    }
} 