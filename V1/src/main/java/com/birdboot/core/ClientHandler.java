package com.birdboot.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/*
* 该线程的任务是与指定的客户端完成http交互*/
public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            int d;  //用来接受读取的每个字节
            while ((d =is.read())!=-1){
                char c  = (char) d;
                System.out.print(c);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
