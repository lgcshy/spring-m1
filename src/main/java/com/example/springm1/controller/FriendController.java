package com.example.springm1.controller;

import com.example.springm1.common.ApiResponse;
import com.example.springm1.entity.User;
import com.example.springm1.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 朋友关系控制器
 */
@RestController
@RequestMapping("/api/friends")
@Tag(name = "好友关系管理", description = "好友关系相关接口")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/request")
    @Operation(summary = "发送好友请求", description = "发送好友请求给指定用户")
    public ResponseEntity<Map<String, Object>> sendFriendRequest(Authentication authentication, @RequestBody Map<String, Long> request) {
        Long currentUserId = Long.valueOf(authentication.getName());
        Long friendId = request.get("friendId");
        
        if (friendId == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("好友ID不能为空"));
        }
        
        if (currentUserId.equals(friendId)) {
            return ResponseEntity.badRequest().body(ApiResponse.error("不能添加自己为好友"));
        }
        
        boolean result = friendService.sendFriendRequest(currentUserId, friendId);
        if (result) {
            return ResponseEntity.ok(ApiResponse.success(true, "好友请求发送成功"));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("好友请求发送失败，可能已经是好友或已发送过请求"));
        }
    }

    @PostMapping("/accept")
    @Operation(summary = "接受好友请求", description = "接受指定用户的好友请求")
    public ResponseEntity<Map<String, Object>> acceptFriendRequest(Authentication authentication, @RequestBody Map<String, Long> request) {
        Long currentUserId = Long.valueOf(authentication.getName());
        Long friendId = request.get("friendId");
        
        if (friendId == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("好友ID不能为空"));
        }
        
        boolean result = friendService.acceptFriendRequest(currentUserId, friendId);
        if (result) {
            return ResponseEntity.ok(ApiResponse.success(true, "好友请求已接受"));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("好友请求接受失败，可能不存在该请求"));
        }
    }

    @PostMapping("/reject")
    @Operation(summary = "拒绝好友请求", description = "拒绝指定用户的好友请求")
    public ResponseEntity<Map<String, Object>> rejectFriendRequest(Authentication authentication, @RequestBody Map<String, Long> request) {
        Long currentUserId = Long.valueOf(authentication.getName());
        Long friendId = request.get("friendId");
        
        if (friendId == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("好友ID不能为空"));
        }
        
        boolean result = friendService.rejectFriendRequest(currentUserId, friendId);
        if (result) {
            return ResponseEntity.ok(ApiResponse.success(true, "好友请求已拒绝"));
        } else {
            return ResponseEntity.badRequest().body(ApiResponse.error("好友请求拒绝失败，可能不存在该请求"));
        }
    }

    @GetMapping("/list")
    @Operation(summary = "获取好友列表", description = "获取当前用户的好友列表")
    public ResponseEntity<Map<String, Object>> getFriends(Authentication authentication) {
        Long currentUserId = Long.valueOf(authentication.getName());
        List<User> friends = friendService.getFriends(currentUserId);
        return ResponseEntity.ok(ApiResponse.success(friends));
    }

    @GetMapping("/requests")
    @Operation(summary = "获取好友请求列表", description = "获取发送给当前用户的好友请求列表")
    public ResponseEntity<Map<String, Object>> getFriendRequests(Authentication authentication) {
        Long currentUserId = Long.valueOf(authentication.getName());
        List<User> requests = friendService.getFriendRequests(currentUserId);
        return ResponseEntity.ok(ApiResponse.success(requests));
    }
} 