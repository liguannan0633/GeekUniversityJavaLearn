package com.geek.learn.nio;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author LiGuanNan
 * @date 2021/7/5 12:08 下午
 * 单线程
 * wrk压测情况:
 *
 */
@Slf4j
public class JavaSocketHttpServer3 {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8802);
        while (true){
            log.info("自旋等待接受客户端请求");
            try {
                Socket accept = serverSocket.accept();
                Thread thread = new Thread(()->{
                    doSomething(accept);
                });
                thread.start();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void doSomething(Socket socket) {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(),true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            String body = "hello nio";
            printWriter.println("Content-Length:" + body.getBytes().length);

            printWriter.println();

            printWriter.write(body);
            printWriter.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
