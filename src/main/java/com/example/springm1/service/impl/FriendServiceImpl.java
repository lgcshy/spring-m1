package com.example.springm1.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springm1.entity.Friend;
import com.example.springm1.entity.FriendStatus;
import com.example.springm1.entity.User;
import com.example.springm1.mapper.FriendMapper;
import com.example.springm1.service.FriendService;
import com.example.springm1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 朋友关系服务实现类
 */
@Service
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements FriendService {

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public boolean sendFriendRequest(Long userId, Long friendId) {
        // 检查是否已经是好友
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Friend::getUserId, userId)
                .eq(Friend::getFriendId, friendId)
                .eq(Friend::getStatus, FriendStatus.ACCEPTED);
        
        if (count(queryWrapper) > 0) {
            return false; // 已经是好友
        }
        
        // 检查是否已经发送过请求
        queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Friend::getUserId, userId)
                .eq(Friend::getFriendId, friendId)
                .eq(Friend::getStatus, FriendStatus.PENDING);
        
        if (count(queryWrapper) > 0) {
            return false; // 已经发送过请求
        }
        
        // 创建好友请求
        Friend friend = new Friend();
        friend.setUserId(userId);
        friend.setFriendId(friendId);
        friend.setStatus(FriendStatus.PENDING);
        
        return save(friend);
    }

    @Override
    @Transactional
    public boolean acceptFriendRequest(Long userId, Long friendId) {
        // 查找好友请求
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Friend::getUserId, friendId)
                .eq(Friend::getFriendId, userId)
                .eq(Friend::getStatus, FriendStatus.PENDING);
        
        Friend friendRequest = getOne(queryWrapper);
        if (friendRequest == null) {
            return false; // 没有找到好友请求
        }
        
        // 更新好友请求状态
        friendRequest.setStatus(FriendStatus.ACCEPTED);
        updateById(friendRequest);
        
        // 创建反向好友关系
        Friend reverseFriend = new Friend();
        reverseFriend.setUserId(userId);
        reverseFriend.setFriendId(friendId);
        reverseFriend.setStatus(FriendStatus.ACCEPTED);
        
        return save(reverseFriend);
    }

    @Override
    @Transactional
    public boolean rejectFriendRequest(Long userId, Long friendId) {
        // 查找好友请求
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Friend::getUserId, friendId)
                .eq(Friend::getFriendId, userId)
                .eq(Friend::getStatus, FriendStatus.PENDING);
        
        Friend friendRequest = getOne(queryWrapper);
        if (friendRequest == null) {
            return false; // 没有找到好友请求
        }
        
        // 更新好友请求状态
        friendRequest.setStatus(FriendStatus.REJECTED);
        
        return updateById(friendRequest);
    }

    @Override
    public List<User> getFriends(Long userId) {
        // 查找所有已接受的好友关系
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Friend::getUserId, userId)
                .eq(Friend::getStatus, FriendStatus.ACCEPTED);
        
        List<Friend> friends = list(queryWrapper);
        if (friends.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取好友ID列表
        List<Long> friendIds = friends.stream()
                .map(Friend::getFriendId)
                .collect(Collectors.toList());
        
        // 查询好友用户信息
        return userService.listByIds(friendIds);
    }

    @Override
    public List<User> getFriendRequests(Long userId) {
        // 查找所有待处理的好友请求
        LambdaQueryWrapper<Friend> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Friend::getFriendId, userId)
                .eq(Friend::getStatus, FriendStatus.PENDING);
        
        List<Friend> friendRequests = list(queryWrapper);
        if (friendRequests.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取请求者ID列表
        List<Long> requestUserIds = friendRequests.stream()
                .map(Friend::getUserId)
                .collect(Collectors.toList());
        
        // 查询请求者用户信息
        return userService.listByIds(requestUserIds);
    }
} 