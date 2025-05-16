package com.example.springm1.controller;

import com.example.springm1.common.ApiResponse;
import com.example.springm1.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存控制器，演示Redis缓存的使用
 */
@RestController
@RequestMapping("/api/cache")
public class CacheController {

    @Autowired
    private RedisService redisService;

    /**
     * 设置缓存
     * @param request 请求参数
     * @return 设置结果
     */
    @PostMapping("/set")
    public ResponseEntity<Map<String, Object>> setCache(@RequestBody Map<String, Object> request) {
        String key = (String) request.get("key");
        Object value = request.get("value");
        Long expireTime = request.get("expireTime") != null ? Long.valueOf(request.get("expireTime").toString()) : null;
        
        try {
            if (expireTime != null && expireTime > 0) {
                redisService.set(key, value, expireTime);
            } else {
                redisService.set(key, value);
            }
            
            return ResponseEntity.ok(ApiResponse.successWithMessage("缓存设置成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("缓存设置失败: " + e.getMessage()));
        }
    }

    /**
     * 获取缓存
     * @param key 缓存键
     * @return 缓存值
     */
    @GetMapping("/get")
    public ResponseEntity<Map<String, Object>> getCache(@RequestParam String key) {
        try {
            Object value = redisService.get(key);
            
            if (value == null) {
                return ResponseEntity.ok(ApiResponse.error("缓存不存在"));
            }
            
            // 获取过期时间
            long expireTime = redisService.getExpire(key);
            
            Map<String, Object> data = new HashMap<>();
            data.put("value", value);
            data.put("expireTime", expireTime);
            
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("获取缓存失败: " + e.getMessage()));
        }
    }

    /**
     * 删除缓存
     * @param key 缓存键
     * @return 删除结果
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteCache(@RequestParam String key) {
        try {
            redisService.delete(key);
            return ResponseEntity.ok(ApiResponse.successWithMessage("缓存删除成功"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error("缓存删除失败: " + e.getMessage()));
        }
    }
} 