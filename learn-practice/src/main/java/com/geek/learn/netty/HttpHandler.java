package com.geek.learn.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

/**
 * @author LiGuanNan
 * @date 2021/7/20 11:16 上午
 */
@Slf4j
public class HttpHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        try {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            String uri = fullHttpRequest.uri();
            if(uri.contains("test")){
                handlerTest(fullHttpRequest,ctx);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void handlerTest(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) {
        FullHttpResponse response = null;
        try {
            String result = "hello 你好";

            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(result.getBytes()));
            response.headers().set("Content-Type","application/json");
            response.headers().setInt("Content-Length",response.content().readableBytes());
        }catch (Exception e){
            log.error("处理出错:");
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.NO_CONTENT);
        }finally {
            if(fullHttpRequest != null){
                if(!HttpUtil.isKeepAlive(fullHttpRequest)){
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                }else {
                    assert response != null;
                    response.headers().set(CONNECTION,KEEP_ALIVE);
                    ctx.write(response);
                }
            }
        }
    }
}
