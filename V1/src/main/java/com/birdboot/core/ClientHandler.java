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
            int d;  //用来接受读取的每个字符
            /*String是不可变类，需要用StringBuffed(线程安全)和StringBuilder(线程不安全)来存储一个可变字符串*/
            StringBuilder builder = new StringBuilder(); //线程不安全,但速度快，保存拼接后的一行字符串
            char cur='a',pre = 'a';  //cur表示本次读取的字符  pre表示上次读取的字符
            while ((d =is.read())!=-1){
                cur  = (char) d;  //将本次读取到的字符赋值给cur
                if(pre==13 && cur == 10){ //判断当前cur是否是换行，pre是否是回车
                    break;  //连续读取到回车换行就停止读取
                }
                builder.append(cur);
                pre = cur; //在读取下一个字符前，将本次读取的字符记作上次读取的字符
            }
            String line = builder.toString().trim();
            System.out.println("请求行："+line);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                /*遵循http通讯要求，关闭连接*/
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
