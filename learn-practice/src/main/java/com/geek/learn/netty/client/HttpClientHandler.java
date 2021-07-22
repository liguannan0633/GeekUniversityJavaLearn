package com.geek.learn.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;


/**
 * @author LiGuanNan
 * @date 2021/7/20 11:16 上午
 */
@Slf4j
public class HttpClientHandler  extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {
        //通道建立完成事件
        //发送请求
        URI uri = new URI("/test");
        FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, uri.toASCIIString());
        request.headers().add(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
        request.headers().add(HttpHeaderNames.CONTENT_LENGTH,request.content().readableBytes());
        ctx.writeAndFlush(request);
        //ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        //响应数据准备就绪事件
        System.out.println("响应msg -> "+msg);
        if(msg instanceof FullHttpResponse){
            FullHttpResponse response = (FullHttpResponse)msg;
            ByteBuf buf = response.content();

            ByteBuf byteBuf = Unpooled.wrappedBuffer(buf);
            String result = byteBuf.toString(CharsetUtil.UTF_8);

            //String result = buf.toString(CharsetUtil.UTF_8);

            System.out.println("响应result -> "+result);
        }
    }
}
