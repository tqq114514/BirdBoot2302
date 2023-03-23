package com.birdboot.http;
/*
* 响应对象
* 响应由三部分构成
* 响应行，响应头，响应正文*/

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HttpServletResponse {
    private Socket socket;
    /*状态行相关属性*/
    private int statusCode = 200;   //状态代码
    private String statusReason = "OK";   //状态描述

    /*响应头相关信息*/

    /*响应正文相关信息*/
    private File contentFile;   //正文对应的文件

    public HttpServletResponse(Socket socket){
        this.socket = socket;
    }

    /*V7 new
    * 发送响应方法，这个方法的内容就是原ClientHandler第三步发送响应的代码*/
    public void response() throws IOException {
        //3发送响应
        /*1、发送状态行*/
        sendStatusLine();
        /*2、发送响应头*/
        sendHeaders();
        /*3、发送正文*/
        sendContent();

    }
    /*发送状态行*/
    private  void sendStatusLine() throws IOException {
        /*1、发送状态行*/
            /*String line = "HTTP/1.1 200 OK";
            out.write(line.getBytes(StandardCharsets.ISO_8859_1));
            out.write(13);
            out.write(10);*/
        println("HTTP/1.1 "+statusCode+" "+statusReason);
    }
    /*发送响应头*/
    private void  sendHeaders() throws IOException {
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
        println("Content-Length: "+contentFile.length());
        /*单独发送回车换行表示响应头发完了*/
            /*out.write(13);
            out.write(10);*/
        println("");
    }
    /*发送响应正文*/
    private void sendContent() throws IOException {
        /*发送响应正文,将index.html页面发送出去*/
        OutputStream out = socket.getOutputStream();
        FileInputStream fis = new FileInputStream(contentFile);
        byte[] data = new byte[1024*10];
        int len;
        while((len=fis.read(data))!=-1){
            out.write(data,0,len);
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

    /*为属性添加get,set方法*/

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public File getContentFile() {
        return contentFile;
    }

    public void setContentFile(File contentFile) {
        this.contentFile = contentFile;
    }
}
