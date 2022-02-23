package com.geek.learn.delay;


import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author cube.li
 * @date 2021/9/22 16:08
 * @description 二维码延时消息处理器
 */
@Component
@Slf4j
public class QrCodeDelayMessageHandler implements DelayMessageHandler {

    @Override
    public void handle(DelayMessage message) {
        log.info("二维码延时消息处理中,message = {}", message.toString());
        QrCode qrCode = JSONObject.parseObject(message.getBody(), QrCode.class);
        //删除二维码
        System.out.println("qrCode: " + qrCode);
    }
}
