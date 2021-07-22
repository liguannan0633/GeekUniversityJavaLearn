package com.geek.learn.netty.filter;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.springframework.util.StringUtils;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.RETRY_AFTER;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HeaderHttpRequestFilter implements HttpRequestFilter {

    @Override
    public Integer filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) {

        //统一jwt验证
        Integer userId = parseToken(fullRequest);

        if(userId != null){
            fullRequest.headers().set("uid", userId);
        }

        return userId;
    }

    private Integer parseToken(FullHttpRequest fullRequest) {
        HttpHeaders headers = fullRequest.headers();
        String token = headers.get("token");
        System.out.println("token: " + token);
        if(StringUtils.hasText(token) && token.equalsIgnoreCase("test")){
            return 999;
        }
        return null;
    }
}
