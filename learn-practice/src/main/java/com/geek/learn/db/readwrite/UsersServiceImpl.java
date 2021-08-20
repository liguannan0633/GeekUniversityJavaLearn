package com.geek.learn.db.readwrite;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.geek.learn.db.readwrite.mapper.UsersMapper;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author LiGuanNan
 * @date 2021/8/19 12:58 下午
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {
    @Override
    public Users findUserByFirstDb(long id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    @CurDataSource(name = DataSourceNames.SECOND)
    public Users findUserBySecondDb(long id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    @CurDataSource(name = DataSourceNames.THREE)
    public Users findUserByThreeDb(long id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public int insert(Users users) {
        return this.baseMapper.insert(users);
    }

    @Override
    public Users findById(Integer id) {
        Users users = null;
        try {
            //loadBalancing();
            users = this.baseMapper.selectById(id);
        }finally {
            //DynamicDataSource.clearDataSource();
        }
        return users;
    }

    private void loadBalancing() {
        Random random = new Random();
        int i = random.nextInt(10);
        int i1 = i % 2;
        if(i1 == 0){
            DynamicDataSource.setDataSource(DataSourceNames.THREE);
        }else {
            DynamicDataSource.setDataSource(DataSourceNames.SECOND);
        }
    }
}
