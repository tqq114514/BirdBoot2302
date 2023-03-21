package com.birdboot.core;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
/*操作少量的数据使用 String。
单线程操作大量数据使用 StringBuilder。
多线程操作大量数据使用 StringBuffer。*/
/*
* 该线程的任务是与指定的客户端完成http交互*/
public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        /*拿到一行请求行的数据，并根据http的要求关闭连接，一问一答的形式*/
        try {
            InputStream is = socket.getInputStream();  //直接从字节流里挨个读数据
            char cur = 'a',pre = 'a';  //分别存储前一行和后一行的数据，并初始化为'a'
            StringBuilder builder = new StringBuilder();  //存储拼接后的可变数组
            int ch;  //存储读到的一个个字符
            while (( ch = is.read())!=-1){
                cur = (char) ch ;  //将当前读到的字符赋值给cur
                if(cur == 13 && pre == 10){  //判断当前读到是不是换行，对应ASCII 13     前一个是不是回车，对应ASCII 10
                    break;
                }
                builder.append(cur);
                pre = cur; //下一次当前的字母就变成前一个了
            }
           String line = builder.toString().trim();
            /*System.out.println("请求行"+line);*/
            /*将请求行中的请求方式，抽象路径，协议版本分别解析出来*/
            String method;
            String url;
            String protocol;
            String[] strings = line.split("[\\s]{1}");  //\s是空白字符
            method = strings[0];
            url = strings[1];
            protocol = strings[2];
            System.out.println("method:"+method);
            System.out.println("url:"+url);
            System.out.println("protocol:"+protocol);




        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
