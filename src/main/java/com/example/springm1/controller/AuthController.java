package com.example.springm1.controller;

import com.example.springm1.common.ApiResponse;
import com.example.springm1.exception.JwtAuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /**
     * 处理JWT认证错误
     * @param request HTTP请求
     * @return 错误响应
     */
    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleAuthError(HttpServletRequest request) {
        JwtAuthenticationException exception = (JwtAuthenticationException) request.getAttribute("jwtException");
        if (exception == null) {
            exception = new JwtAuthenticationException("未知的认证错误");
        }
        
        return ResponseEntity
                .status(exception.getStatus())
                .body(ApiResponse.error(exception.getMessage()));
    }
} 