package com.geek.learn;

import com.alibaba.fastjson.JSON;
import com.geek.learn.delay.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class LearnPracticeApplicationTests {

    @Test
    void contextLoads() {
    }

    @Resource(type = RedissonDelayMessageManager.class)
    DelayMessageManager delayMessageManager;

    @org.junit.Test
    public void add() throws Exception {
        DelayMessage delayMessage = new DelayMessage();
        QrCode qrCode = new QrCode();
        qrCode.setConfigId("fadfdaf110");
        qrCode.setUrl("http://www.baidu.com");
        delayMessage.setBody(JSON.toJSONString(qrCode));
        delayMessage.setType(DelayMessageType.DELETE_QR_CODE);
        DelayMessage.DelayMessageProperties properties = new DelayMessage.DelayMessageProperties();
        properties.setExpire(10);
        properties.setTimeUnit(TimeUnit.SECONDS);
        delayMessage.setProperties(properties);
        delayMessageManager.add(delayMessage);

        DelayMessage delayMessage1 = new DelayMessage();
        QrCode qrCode1 = new QrCode();
        qrCode1.setConfigId("fadfdaf1405");
        qrCode1.setUrl("http://www.baidu.com");
        delayMessage1.setBody(JSON.toJSONString(qrCode1));
        delayMessage1.setType(DelayMessageType.DELETE_QR_CODE);
        DelayMessage.DelayMessageProperties properties1 = new DelayMessage.DelayMessageProperties();
        properties1.setExpire(5);
        properties1.setTimeUnit(TimeUnit.SECONDS);
        delayMessage1.setProperties(properties1);
        delayMessageManager.add(delayMessage1);

        DelayMessage delayMessage2 = new DelayMessage();
        ExecuteTask task = new ExecuteTask();
        task.setId(1L);
        /*task.setTask(()->{
            System.out.println("执行用户自定义的一段任务逻辑");
        });*/
        delayMessage2.setBody(JSON.toJSONString(task));
        delayMessage2.setType(DelayMessageType.EXECUTE_TASK);
        DelayMessage.DelayMessageProperties properties2 = new DelayMessage.DelayMessageProperties();
        properties2.setExpire(4);
        properties2.setTimeUnit(TimeUnit.SECONDS);
        delayMessage2.setProperties(properties2);
        delayMessageManager.add(delayMessage2);

        //删除一个任务在他执行之前
        delayMessageManager.remove(delayMessage1);

        TimeUnit.SECONDS.sleep(20);
        System.out.println("end......");
    }
}
