package com.example.springm1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springm1.entity.Friend;
import com.example.springm1.entity.User;

import java.util.List;

/**
 * 朋友关系服务接口
 */
public interface FriendService extends IService<Friend> {
    
    /**
     * 发送好友请求
     * @param userId 用户ID
     * @param friendId 朋友ID
     * @return 是否成功
     */
    boolean sendFriendRequest(Long userId, Long friendId);
    
    /**
     * 接受好友请求
     * @param userId 用户ID
     * @param friendId 朋友ID
     * @return 是否成功
     */
    boolean acceptFriendRequest(Long userId, Long friendId);
    
    /**
     * 拒绝好友请求
     * @param userId 用户ID
     * @param friendId 朋友ID
     * @return 是否成功
     */
    boolean rejectFriendRequest(Long userId, Long friendId);
    
    /**
     * 获取用户的好友列表
     * @param userId 用户ID
     * @return 好友列表
     */
    List<User> getFriends(Long userId);
    
    /**
     * 获取用户的好友请求列表
     * @param userId 用户ID
     * @return 好友请求列表
     */
    List<User> getFriendRequests(Long userId);
} 