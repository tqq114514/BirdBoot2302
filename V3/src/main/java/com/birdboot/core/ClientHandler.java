package com.birdboot.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 该线程的任务是与指定的客户端完成HTTP交互
 * HTTP协议要求一问一答
 * 对此,这里的处理大致分为三步
 * 1:解析请求
 * 2:处理请求
 * 3:发送响应
 */
public class ClientHandler implements Runnable{
    private Socket socket;
    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    public void run() {
        try {
            //1解析请求
            //1.1解析请求行
            String line = readLine();//V3新加内容
            System.out.println("请求行\t"+line);
            String method;
            String uri;
            String protocol;
            String[] data = line.split("\\s");
            method = data[0];
            uri = data[1];
            protocol = data[2];
            //http://localhost:8088/index.html
            System.out.println("method:"+method);
            System.out.println("uri:"+uri);
            System.out.println("protocol:"+protocol);

            //V3新加内容:
            //1.2解析消息头
            Map<String,String> headers = new HashMap<>();
            while(true) {
                line = readLine();
                /*
                    HTTP协议要求,消息头发送的个数由浏览器自行决定,但是当所有消息头都发送
                    完毕后,要给服务端单独发送一个回车+换行表达消息头部分发送完毕
                    因此,这里我们采取循环读取的策略,若单独读取到了回车+换行就停止循环

                    按照我们readLine方法的实现逻辑,如果单独读取到了回车+换行,那么方法
                    返回时:builder.toString()只有一个回车符,然而trim后就变成空字符串了
                    因此若readLine方法返回一个空字符串时,可以断定单独读取到了回车+换行
                 */
                if(line.isEmpty()){
                    break;
                }
                System.out.println("消息头\t" + line);
                /*将消息头按照": "进行拆分并存入headers中*/
                String[] headerData = line.split(":\\s");
                headers.put(headerData[0],headerData[1]);
            }
            System.out.println(headers);

            //2处理请求

            //3发送响应

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //遵循HTTP协议要求,一问一答后与客户端断开连接
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * V3版本新增:
     * 将V2版本中run方法里测试读取一行字符串的操作封装成一个方法
     * 以便解析请求时,解析请求行和消息头的操作复用
     *
     * tips:通常被复用的代码不会自己处理异常
     * @return
     */
    private String readLine() throws IOException {
        /*
            socket对象没有发生改变,那么无论调用这个对象多少次获取流的操作时,获取的始终是
            同一条流(输入,输出都一样)
         */
        InputStream in = socket.getInputStream();
        int d;
        StringBuilder builder = new StringBuilder();//保存拼接后的一行字符串
        char cur='a',pre='a';//cur表示本次读取的字符  pre表示上次读取的字符
        while((d = in.read())!=-1){
            cur = (char)d;//将本次读取的字符赋值给cur
            if(pre==13 && cur==10){//判断上次是否读取的回车符,本次是否为换行符
                break;//若连续读取了回车+换行就停止读取(一行结束了)
            }
            builder.append(cur);
            pre=cur;//在读取下一个字符前,将本次读取的字符记作"上次读取的字符"
        }
        return builder.toString().trim();
    }
}