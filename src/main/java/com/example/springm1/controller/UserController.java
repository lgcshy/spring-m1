package com.example.springm1.controller;

import com.example.springm1.common.ApiResponse;
import com.example.springm1.entity.User;
import com.example.springm1.service.RedisService;
import com.example.springm1.service.UserService;
import com.example.springm1.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisService redisService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody User user) {
        // 检查用户名是否已存在
        User existingUser = userService.getUserByUsername(user.getUsername());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("用户名已存在"));
        }
        
        // 设置创建时间和更新时间
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        user.setDeleted(0); // 未删除
        
        // 使用Spring Security的密码编码器加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // 保存用户
        userService.save(user);
        
        return ResponseEntity.ok(ApiResponse.successWithMessage("注册成功"));
    }

    /**
     * 用户登录
     * @param loginRequest 登录请求
     * @return 登录结果
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        try {
            // 使用Spring Security进行认证
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            
            // 设置认证信息到上下文
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 生成token
            String token = jwtUtil.generateToken(username);
            
            // 将token存入Redis，设置过期时间为24小时
            String redisKey = "token:" + username;
            redisService.set(redisKey, token, 24 * 60 * 60);
            
            // 获取用户信息
            User user = userService.getUserByUsername(username);
            
            // 构建响应数据
            Map<String, Object> data = Map.of(
                    "token", token,
                    "user", user
            );
            
            return ResponseEntity.ok(ApiResponse.success(data, "登录成功"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("用户名或密码错误"));
        }
    }

    /**
     * 获取用户信息
     * @param principal 当前认证的用户
     * @return 用户信息
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getUserInfo(Principal principal) {
        try {
            // 从Principal获取用户名
            String username = principal.getName();
            
            // 获取用户信息
            User user = userService.getUserByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("用户不存在"));
            }
            
            return ResponseEntity.ok(ApiResponse.success(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("获取用户信息失败: " + e.getMessage()));
        }
    }

    /**
     * 退出登录
     * @param principal 当前认证的用户
     * @return 退出结果
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(Principal principal) {
        try {
            // 从Principal获取用户名
            String username = principal.getName();
            
            // 从Redis中删除token
            String redisKey = "token:" + username;
            redisService.delete(redisKey);
            
            // 清除Security上下文
            SecurityContextHolder.clearContext();
            
            return ResponseEntity.ok(ApiResponse.successWithMessage("退出成功"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("退出失败: " + e.getMessage()));
        }
    }
} 