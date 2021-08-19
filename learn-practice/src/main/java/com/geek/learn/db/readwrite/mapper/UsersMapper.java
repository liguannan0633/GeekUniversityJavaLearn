package com.geek.learn.db.readwrite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.geek.learn.db.readwrite.Users;

public interface UsersMapper extends BaseMapper<Users> {
    int insert(Users record);

    int insertSelective(Users record);
}