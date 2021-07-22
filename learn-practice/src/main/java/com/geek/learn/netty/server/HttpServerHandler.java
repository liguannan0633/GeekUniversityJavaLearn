package com.geek.learn.netty.server;

import com.geek.learn.netty.HttpClientTest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_PLAIN;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_0;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

/**
 * @author LiGuanNan
 * @date 2021/7/20 11:16 上午
 */
@Slf4j
public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        //粘包拆包
//        ByteBuf byteBuf = (ByteBuf) msg;
//        byte[] content = new byte[byteBuf.readableBytes()];
//        byteBuf.readBytes(content);
//        System.out.println(Thread.currentThread() + ": 最终打印: " + new String(content));
//        ((ByteBuf) msg).release();
        try {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            System.out.println();
            String uri = fullHttpRequest.uri();
            System.out.println("uri : " + uri);

            ByteBuf byteBuf = fullHttpRequest.content();
            byte[] content = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(content);
            System.out.println(Thread.currentThread() + ": content: " + new String(content));

            if(uri.contains("test")){
                handlerTest(fullHttpRequest,ctx);
            }else {
                handlerTest(fullHttpRequest,ctx);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    private void handlerTest(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx) {
        FullHttpResponse response = null;
        try {
            String result = "hello 你好";
            //httpclient发送请求,执行业务逻辑
            result = HttpClientTest.doGetTestOne("https://room.neibu.koolearn.com/api/room/live-notice?roomId=1");
            if(StringUtils.hasText(result)){
                response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(result.getBytes()));
                response.headers().set("Content-Type","application/json");
                response.headers().setInt("Content-Length",response.content().readableBytes());
            }
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