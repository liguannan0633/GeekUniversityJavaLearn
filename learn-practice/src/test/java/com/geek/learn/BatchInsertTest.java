package com.geek.learn;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author LiGuanNan
 * @date 2021/8/12 4:01 下午
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BatchInsertTest {
    /*private String url ="jdbc:mysql://127.0.0.1:3307/sharding_db?rewriteBatchedStatements=true";

    private String user ="root";

    private String password ="root";*/


    private String url ="jdbc:mysql://10.155.19.189:4901/gn_yanci?rewriteBatchedStatements=true";

    private String user ="guonei_ceshi";

    private String password ="CeShi*0409.";

    @BeforeClass
    public static void initEnvParams() {
        String env = System.getProperty("env");
        if (StringUtils.isEmpty(env)) {
            System.setProperty("env", "neibu");
        }

        String appId = System.getProperty("app.id");
        if (StringUtils.isEmpty(appId)) {
            System.setProperty("app.id", "koo-guonei-yanci-webapp");
        }
    }

    @Test
    public void jdbcInsert(){

        Connection conn =null;

        PreparedStatement pstm =null;

        ResultSet rt =null;

        long userCount = 1000000;
        long orderCount = 10000000;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = (Connection) DriverManager.getConnection(url,user,password);

            String sql ="insert into t_order (user_id,status) VALUES(?,1)";

            pstm = (PreparedStatement) conn.prepareStatement(sql);

            long start = System.currentTimeMillis();
            System.out.println("start ............... ");

            for (int i =0; i <orderCount; i++) {

                BigDecimal a = BigDecimal.valueOf(Math.random());
                int userId  = a.multiply(new BigDecimal(userCount)).intValue();

                pstm.setInt(1,userId);

                pstm.addBatch();

            }

            pstm.executeBatch();

            long end = System.currentTimeMillis();

            System.out.println("end ............... ");
            System.out.println("用时："+(end-start));

        }catch (Exception e) {

            e.printStackTrace();

        }finally {

            if (conn!=null){

                try {

                    conn.close();

                }catch (Exception e) {

                    e.printStackTrace();

                }

            }

            if (pstm!=null){

                try {

                    pstm.close();

                }catch (Exception e) {

                    e.printStackTrace();

                }

            }

        }

    }

}

