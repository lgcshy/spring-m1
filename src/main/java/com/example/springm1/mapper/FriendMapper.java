package com.example.springm1.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springm1.entity.Friend;
import org.apache.ibatis.annotations.Mapper;

/**
 * 朋友关系Mapper接口
 */
@Mapper
public interface FriendMapper extends BaseMapper<Friend> {
} 