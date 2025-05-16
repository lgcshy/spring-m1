package com.example.springm1.common;

import java.util.HashMap;
import java.util.Map;

/**
 * API统一响应封装
 */
public class ApiResponse {
    
    /**
     * 创建成功响应
     * @return 响应对象
     */
    public static Map<String, Object> success() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return response;
    }
    
    /**
     * 创建带数据的成功响应
     * @param data 响应数据
     * @return 响应对象
     */
    public static Map<String, Object> success(Object data) {
        Map<String, Object> response = success();
        response.put("data", data);
        return response;
    }
    
    /**
     * 创建带消息的成功响应
     * @param message 成功消息
     * @return 响应对象
     */
    public static Map<String, Object> successWithMessage(String message) {
        Map<String, Object> response = success();
        response.put("message", message);
        return response;
    }
    
    /**
     * 创建带数据和消息的成功响应
     * @param data 响应数据
     * @param message 成功消息
     * @return 响应对象
     */
    public static Map<String, Object> success(Object data, String message) {
        Map<String, Object> response = success();
        response.put("data", data);
        response.put("message", message);
        return response;
    }
    
    /**
     * 创建失败响应
     * @param message 错误消息
     * @return 响应对象
     */
    public static Map<String, Object> error(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
    
    /**
     * 创建带错误码的失败响应
     * @param message 错误消息
     * @param errorCode 错误码
     * @return 响应对象
     */
    public static Map<String, Object> error(String message, String errorCode) {
        Map<String, Object> response = error(message);
        response.put("errorCode", errorCode);
        return response;
    }
} 