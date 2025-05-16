package com.example.springm1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springm1.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 继承BaseMapper后，已经有基本的CRUD方法
    // 可以在这里添加自定义的查询方法
} 