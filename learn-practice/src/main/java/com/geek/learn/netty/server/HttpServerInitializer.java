package com.geek.learn.netty.server;

import com.geek.learn.netty.server.HttpServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @author LiGuanNan
 * @date 2021/7/20 11:08 上午
 */
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        //绑定Handler到Channel的ChannelPipeline
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        //注意: 必须设置HttpObjectAggregator 不然handler接受到的请求msg不是FullHttpRequest类型(因为如果不设置,是分片接受请求信息的,设置了就忽略分片,一次获取全部)
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
        //自定义解码器(测试粘包拆包)
        //pipeline.addLast(new HttpDecoder());
        pipeline.addLast(new HttpServerHandler());
    }
}
