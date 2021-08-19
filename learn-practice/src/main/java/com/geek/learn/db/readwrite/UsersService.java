package com.geek.learn.db.readwrite;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author LiGuanNan
 * @date 2021/8/19 12:58 下午
 */
public interface UsersService extends IService<Users> {

    Users findUserByFirstDb(long id);

    Users findUserBySecondDb(long id);

    Users findUserByThreeDb(long id);

    int insert(Users users);

    Users findById(Integer id);

}
