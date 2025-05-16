package com.example.springm1.config;

import com.example.springm1.exception.JwtAuthenticationException;
import com.example.springm1.service.RedisService;
import com.example.springm1.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * JWT认证过滤器
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisService redisService;

    // 不需要验证token的路径
    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/api/users/login",
            "/api/users/register",
            "/api/auth/error"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 如果是登录或注册请求，直接放行
        String path = request.getRequestURI();
        if (EXCLUDE_PATHS.stream().anyMatch(path::contains)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        try {
            // 从请求头中获取token
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new JwtAuthenticationException("未提供token");
            }
            
            String token = authHeader.replace("Bearer ", "");
            
            // 从token中获取用户名
            String username = jwtUtil.getUsernameFromToken(token);
            
            // 验证token是否有效
            if (!jwtUtil.validateToken(token, username)) {
                throw new JwtAuthenticationException("无效的token");
            }
            
            // 从Redis中获取token
            String redisKey = "token:" + username;
            String cachedToken = (String) redisService.get(redisKey);
            
            // 验证token是否与Redis中的一致
            if (cachedToken == null || !cachedToken.equals(token)) {
                throw new JwtAuthenticationException("token已过期或无效");
            }
            
            // 验证通过，放行
            filterChain.doFilter(request, response);
        } catch (JwtAuthenticationException e) {
            // 这里不直接写响应，而是通过Spring的异常处理机制处理
            request.setAttribute("jwtException", e);
            // 将异常传递给Spring的异常处理器
            request.getRequestDispatcher("/api/auth/error").forward(request, response);
        } catch (Exception e) {
            // 处理其他异常
            JwtAuthenticationException jwtException = new JwtAuthenticationException("token验证失败: " + e.getMessage());
            request.setAttribute("jwtException", jwtException);
            request.getRequestDispatcher("/api/auth/error").forward(request, response);
        }
    }
} 