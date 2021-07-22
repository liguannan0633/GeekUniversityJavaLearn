package com.geek.learn.netty.client;

import com.geek.learn.netty.client.HttpClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author LiGuanNan
 * @date 2021/7/22 11:51 上午
 */
public class NettyHttpClient {

    public static void main(String[] args) {
        //服务端地址
        //"https://room.neibu.koolearn.com/api/room/live-notice?roomId=1";
        String host = "127.0.0.1";
        //服务端端口
        int port = 8808;

        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup(8);

        try{
            //netty 启动入口
            //创建ClientBootStrap
            Bootstrap b = new Bootstrap();
            b.option(ChannelOption.SO_KEEPALIVE,true);

            //指定使用NIO传输Channel
            b.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //绑定Handler到Channel的ChannelPipeline
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 客户端接收到的是httpResponse响应，所以要使用HttpResponseDecoder进行解码
                            ch.pipeline().addLast(new HttpResponseDecoder());
                            //客户端发送的是httpRequest，所以要使用HttpRequestEncoder进行编码
                            ch.pipeline().addLast(new HttpRequestEncoder());
                            //ch.pipeline().addLast(new HttpServerCodec());
                            //注意: 必须设置HttpObjectAggregator 不然handler接受到的响应msg不是FullHttpResponse类型(因为如果不设置,是分片接受响应信息的,设置了就忽略分片,一次获取全部)
                            ch.pipeline().addLast(new HttpObjectAggregator(1024 * 1024));
                            ch.pipeline().addLast(new HttpContentDecompressor());

                            ch.pipeline().addLast(new HttpClientHandler());
                        }
                    });

            // Start the client.
            // 异步绑定端口号，需要阻塞住直到端口号绑定成功
            ChannelFuture f = b.connect(host, port).sync();

            f.channel().flush();

            f.channel().closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
