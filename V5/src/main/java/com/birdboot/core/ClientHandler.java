package com.birdboot.core;

import com.birdboot.http.HttpServletRequest;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
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
public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        /*1、解析请求*/
        /*2、处理请求*/
        /*3、发送响应*/
        try {
            //1解析请求
            HttpServletRequest request = new HttpServletRequest(socket);
            String uri = request.getUri();
            System.out.println("请求的uri是："+uri);
            String method = request.getMethod();
            System.out.println("请求方式是："+method);
            String protocol = request.getProtocol();
            System.out.println("协议类型为："+protocol);


            //2处理请求

            /*v5新增
            * 将首页包含在响应中*/
            /*实际开发中的相对路径：类加载路径*/
            /*定位类加载路径*/
            File baseDir = new File(
                    ClientHandler.class.getClassLoader().getResource(".").toURI()
            );
            /*根据类加载路径定位其中的static目录*/
            File staticDir = new File(baseDir,"static");
            /*根据static目录定位其中的index,html目录*/
            File file = new File(staticDir,uri);

            /*如果请求页面不存在，则返回404*/
            int statusCode;  //状态码
            String statusReason;  //状态描述
            if(file.isFile()) {
                statusCode = 200;
                statusReason = "OK";
            }else {
                statusCode = 404;
                statusReason = "NotFound";
                file = new File(staticDir,"404.html");
            }
                 /*
                将页面包含在响应中发送给浏览器
                HTTP/1.1 200 OK(CRLF)
                Content-Type: text/html(CRLF)
                Content-Length: 2546(CRLF)(CRLF)
                1011101010101010101......
             */

                //3发送响应

                /*1、发送状态行*/
            /*String line = "HTTP/1.1 200 OK";
            out.write(line.getBytes(StandardCharsets.ISO_8859_1));
            out.write(13);
            out.write(10);*/
                println("HTTP/1.1 "+statusCode+" "+statusReason);
                /*2、发送响应头*/
           /* line = "Content-Type: text/html";
            out.write(line.getBytes(StandardCharsets.ISO_8859_1));
            out.write(13);
            out.write(10);*/
                println("Content-Type: text/html");
           /* line = "Content-Length: "+file.length();
            out.write(line.getBytes(StandardCharsets.ISO_8859_1));
            out.write(13);
            out.write(10);*/
                println("Content-Length: "+file.length());
                /*单独发送回车换行表示响应头发完了*/
            /*out.write(13);
            out.write(10);*/
                println("");

                /*发送响应正文,将index.html页面发送出去*/
                OutputStream out = socket.getOutputStream();
                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[1024*10];
                int len;
                while((len=fis.read(data))!=-1){
                    out.write(data,0,len);
                }

        } catch (IOException | URISyntaxException e) {
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
    /*发送响应时，状态行和响应头发送一行字符串的代码是一致的，因此单独独立建立方法进行处理*/
    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        /*1、发送状态行*/
        out.write(line.getBytes(StandardCharsets.ISO_8859_1));
        out.write(13);
        out.write(10);
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
