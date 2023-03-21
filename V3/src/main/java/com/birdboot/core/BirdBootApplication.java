package com.birdboot.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Handler;

/*项目的主启动类*/
public class BirdBootApplication {
    private ServerSocket serverSocket;
    public BirdBootApplication(){
        try {
            serverSocket = new ServerSocket(8088);
            System.out.println("服务端已启动");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start(){
        try {
            System.out.println("等待客户端连接");
            Socket socket = serverSocket.accept();
            System.out.println("一个客户端连接已建立");
            ClientHandler handler = new ClientHandler(socket);
            Thread thread = new Thread(handler);
            thread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        BirdBootApplication application = new BirdBootApplication();
        application.start();
    }
}
