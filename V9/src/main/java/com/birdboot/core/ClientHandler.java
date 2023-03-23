package com.birdboot.core;

import com.birdboot.http.HttpServletRequest;
import com.birdboot.http.HttpServletResponse;

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
            /*在处理请求前应实例化响应对象*/
            HttpServletResponse response = new HttpServletResponse(socket);


            /*2、处理请求*/

            DispatchServlet.getInstance().service(request,response);

                 /*
                将页面包含在响应中发送给浏览器
                HTTP/1.1 200 OK(CRLF)
                Content-Type: text/html(CRLF)
                Content-Length: 2546(CRLF)(CRLF)
                1011101010101010101......
             */
            /*3、发送响应*/
            /*发送响应的操作已经移动至HttpServletResponse的response()方法中，直接调用即可*/
            response.response();


        } catch (IOException  e) {
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

}

    /**
     * V3版本新增:
     * 将V2版本中run方法里测试读取一行字符串的操作封装成一个方法
     * 以便解析请求时,解析请求行和消息头的操作复用
     *
     * tips:通常被复用的代码不会自己处理异常
     * @return
     */
