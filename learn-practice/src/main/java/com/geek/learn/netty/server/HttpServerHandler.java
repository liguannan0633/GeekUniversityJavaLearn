package com.geek.learn.netty.server;

import com.geek.learn.netty.HttpClientTest;
import com.geek.learn.netty.filter.HttpRequestFilter;
import com.geek.learn.netty.filter.JWTFilter;
import com.geek.learn.netty.filter.HeaderHttpResponseFilter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

/**
 * @author LiGuanNan
 * @date 2021/7/20 11:16 上午
 */
@Slf4j
public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    private HttpOutboundHandler handler;

    private HttpRequestFilter filter = new JWTFilter();

    public HttpServerHandler() {
        this.handler = new HttpOutboundHandler(Arrays.asList("https://room.neibu.koolearn.com"));
    }

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
            System.out.println("msg: " + msg);
            if(msg instanceof FullHttpRequest){
                FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
                System.out.println();
                String uri = fullHttpRequest.uri();
                System.out.println("uri : " + uri);

                //添加filter
                /*Integer uid = filter.filter(fullHttpRequest, ctx);

                //认证通过 才能继续业务处理
                if(uid != null){
                    System.out.println("用户cookie: " + fullHttpRequest.headers().get("Cookie"));

                    if(uri.contains("api")){
                        //httpclient发送请求,执行业务逻辑
                        String result = HttpClientTest.doGetTestOne("https://room.neibu.koolearn.com" + uri);
                        //将业务执行结果响应回去
                        handlerTest(fullHttpRequest,ctx,result);
                    }else {
                        handlerTest(fullHttpRequest,ctx,"hello,其他");
                    }
                }else {
                    handlerTest(fullHttpRequest,ctx,"非法用户");
                }*/

                //使用带路由功能的,异步的,带连接池的httpclient请求
                handler.handle(fullHttpRequest,ctx,filter);

            }else {
                System.out.println("msg 类型不匹配哦");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }

    /*@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }*/

    private void handlerTest(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx, String str) {
        FullHttpResponse response = null;
        try {
            String result = str;
            if(StringUtils.hasText(result)){
                response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(result.getBytes()));
                response.headers().set("Content-Type","application/json");
                response.headers().setInt("Content-Length",response.content().readableBytes());
            }

            //响应过滤器
            if(response != null){
                HeaderHttpResponseFilter responseFilter = new HeaderHttpResponseFilter();
                responseFilter.filter(response);
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
