package com.example.springm1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springm1.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    // 继承IService后，已经有基本的CRUD方法
    // 可以在这里添加自定义的业务方法
    
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
    User getUserByUsername(String username);
} 