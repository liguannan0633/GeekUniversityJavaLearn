package com.geek.learn.netty.filter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface HttpRequestFilter {

    Integer filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx);
    
}
