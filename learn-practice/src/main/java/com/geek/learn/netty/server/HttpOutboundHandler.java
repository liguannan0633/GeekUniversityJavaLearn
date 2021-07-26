package com.geek.learn.netty.server;


import com.geek.learn.netty.filter.HeaderHttpResponseFilter;
import com.geek.learn.netty.filter.HttpRequestFilter;
import com.geek.learn.netty.filter.HttpResponseFilter;
import com.geek.learn.netty.router.HttpEndpointRouter;
import com.geek.learn.netty.router.RandomHttpEndpointRouter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpOutboundHandler {
    
    private CloseableHttpAsyncClient httpclient;
    private ExecutorService proxyService;
    private List<String> backendUrls;

    HttpResponseFilter filter = new HeaderHttpResponseFilter();
    HttpEndpointRouter router = new RandomHttpEndpointRouter();

    public HttpOutboundHandler(List<String> backends) {

        this.backendUrls = backends.stream().map(this::formatUrl).collect(Collectors.toList());

        int cores = Runtime.getRuntime().availableProcessors();
        long keepAliveTime = 1000;
        int queueSize = 500;
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        proxyService = new ThreadPoolExecutor(cores * 2, cores * 4,
                keepAliveTime, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(queueSize),
                new NamedThreadFactory("proxyService"), handler);
        
        IOReactorConfig ioConfig = IOReactorConfig.custom()
                .setConnectTimeout(1000)
                .setSoTimeout(1000)
                .setIoThreadCount(cores)
                .setRcvBufSize(32 * 1024)
                .build();
        
        httpclient = HttpAsyncClients.custom()
                //连接池中最大连接数;
                .setMaxConnTotal(40)
                //分配给同一个route(路由)最大的并发连接数,route为运行环境机器到目标机器的一条线路
                .setMaxConnPerRoute(8)
                .setDefaultIOReactorConfig(ioConfig)
                .setKeepAliveStrategy((response,context) -> 6000)
                .build();
        httpclient.start();
    }

    private String formatUrl(String backend) {
        return backend.endsWith("/")?backend.substring(0,backend.length()-1):backend;
    }
    
    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final HttpRequestFilter filter) {
        String backendUrl = router.route(this.backendUrls);
        final String url = backendUrl + fullRequest.uri();
        filter.filter(fullRequest, ctx);
        proxyService.submit(()->fetchGet(fullRequest, ctx, url));
    }
    
    private void fetchGet(final FullHttpRequest inbound, final ChannelHandlerContext ctx, final String url) {
        final HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_KEEP_ALIVE);
        httpGet.setHeader("Cookie", inbound.headers().get("Cookie"));

        httpclient.execute(httpGet, new FutureCallback<HttpResponse>() {
            @Override
            public void completed(final HttpResponse endpointResponse) {
                try {
                    System.out.println("业务数据请求响应成功");
                    handleResponse(inbound, ctx, endpointResponse);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("业务数据->网关响应");
                }
            }
            
            @Override
            public void failed(final Exception ex) {
                httpGet.abort();
                ex.printStackTrace();
            }
            
            @Override
            public void cancelled() {
                httpGet.abort();
            }
        });
    }

    private void handleResponse(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, final HttpResponse endpointResponse) {
        FullHttpResponse response = null;
        try {
            byte[] body = EntityUtils.toByteArray(endpointResponse.getEntity());
            System.out.println("业务服务响应状态: " + endpointResponse.getStatusLine());
            System.out.println("业务服务响应数据: " + new String(body));
            System.out.println("业务服务响应数据长度: " + body.length);

            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(body));
            for (Header e : endpointResponse.getAllHeaders()) {
                System.out.println(e.getName() + " => " + e.getValue());
                response.headers().set(e.getName(),e.getValue());
            }

            response.headers().set("Content-Type", "application/json");
            //response.headers().set("Transfer-encoding", "chunked");//业务服务响应头设置的,所以保持一致
            //不设置Content-Length 是因为业务服务响应头中没有响应Content-Length, 并且设置了Transfer-encoding=chunked,表示输出的内容长度不能确定
            //response.headers().setInt("Content-Length", Integer.parseInt(endpointResponse.getFirstHeader("Content-Length").getValue()));

            filter.filter(response);

        } catch (Exception e) {
            e.printStackTrace();
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
            exceptionCaught(ctx, e);
        } finally {
            if(fullRequest != null){
                if(!HttpUtil.isKeepAlive(fullRequest)){
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                }else {
                    ctx.write(response);
                }
            }
            ctx.flush();
        }
        
    }
    
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
    
    
}
