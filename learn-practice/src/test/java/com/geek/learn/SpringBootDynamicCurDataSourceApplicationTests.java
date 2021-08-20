package com.geek.learn;

import com.geek.learn.db.readwrite.Users;
import com.geek.learn.db.readwrite.UsersService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author LiGuanNan
 * @date 2021/8/19 1:03 下午
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringBootDynamicCurDataSourceApplicationTests {

    @Resource
    private UsersService userService;

    @Test
    public void test1() {
        Users user = userService.findUserByFirstDb(1);
        log.info("第一个数据库 : [{}]", user.toString());
        Users user2 = userService.findUserBySecondDb(1);
        log.info("第二个数据库 : [{}]", user2.toString());
        Users user3 = userService.findUserByThreeDb(1);
        log.info("第3个数据库 : [{}]", user3.toString());
    }

    @Test
    public void test2() throws Exception{
        Users users = new Users();
        users.setId(6L);
        users.setName("ss");
        users.setComment("dd");
        userService.insert(users);
    }

    @Test
    public void test3() throws Exception{
        Users byId = userService.findById(1);
        System.out.println(byId.toString());
        Users byId2 = userService.findById(1);
        System.out.println(byId2.toString());
        Users byId1 = userService.findById(1);
        System.out.println(byId1.toString());
    }
}
