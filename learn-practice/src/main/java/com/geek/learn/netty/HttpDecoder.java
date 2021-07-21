package com.geek.learn.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 自定义解码器(用于粘包拆包)
 * @author LiGuanNan
 * @date 2021/7/21 4:13 下午
 */
public class HttpDecoder extends ByteToMessageDecoder {

    static final int PACKET_SIZE = 220;

    // 用来临时保留没有处理过的请求报文
    ByteBuf tempMsg = Unpooled.buffer();

    /**
     * @param ctx
     * @param in 请求的数据
     * @param out 将粘在一起的报文拆分后的结果保留起来
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println(Thread.currentThread() + "收到了一次数据包，长度是：" + in.readableBytes());

        // 合并报文
        ByteBuf message = null;
        int tmpMsgSize = tempMsg.readableBytes();
        // 如果暂存有上一次余下的请求报文，则合并
        if (tmpMsgSize > 0) {
            message = Unpooled.buffer();
            message.writeBytes(tempMsg);
            message.writeBytes(in);
            System.out.println("合并：上一数据包余下的长度为：" + tmpMsgSize + ",合并后长度为:" + message.readableBytes());
        } else {
            message = in;
        }

        int size = message.readableBytes();
        int counter = size / PACKET_SIZE;
        for (int i = 0; i < counter; i++) {
            byte[] request = new byte[PACKET_SIZE];
            // 每次从总的消息中读取220个字节的数据
            message.readBytes(request);

            // 将拆分后的结果放入out列表中，交由后面的业务逻辑去处理
            out.add(Unpooled.copiedBuffer(request));
        }

        // 多余的报文存起来
        // 第一个报文： i+  暂存
        // 第二个报文： 1 与第一次
        size = message.readableBytes();
        if (size != 0) {
            System.out.println("多余的数据长度：" + size);
            // 剩下来的数据放到tempMsg暂存
            //tempMsg.clear();
            tempMsg.writeBytes(message.readBytes(size));
        }
    }
}

