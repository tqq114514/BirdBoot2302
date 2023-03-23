package com.birdboot.core;

/*DispatchServlet是SpringMVC框架提供的一个Servlet类，用于与Tomcat整合使用的类
* 具体作用是接受处理请求工作
* Tomcat中处理请求的类都要继承HttpServlet,实现该类都要重写service方法
* SpringMVC
* */

import com.birdboot.http.HttpServletRequest;
import com.birdboot.http.HttpServletResponse;

import java.io.File;
import java.net.URISyntaxException;

public class DispatchServlet {
    private static DispatchServlet instance = new DispatchServlet();
    /*由于类加载路径和其下的static目录是固定的，则表示这两个目录的File对象可以定义成静态的，全局一份即可，不用
       每次处理请求都实例化一次
       *
       *
       * */
    private static File baseDir;
    private static File staticDir;
    static {
        try {
            baseDir = new File(
                   DispatchServlet.class.getClassLoader().getResource(".").toURI()
           );
            /*根据类加载路径定位其中的static目录*/
            staticDir = new File(baseDir,"static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private DispatchServlet(){}

    public void service(HttpServletRequest request, HttpServletResponse response){

        //2处理请求

        /*v5新增
         * 将首页包含在响应中*/
        /*实际开发中的相对路径：类加载路径*/
        /*定位类加载路径*/

        /*根据static目录定位其中的index,html目录*/
        String uri = request.getUri();
        System.out.println("请求的uri是："+uri);
        String method = request.getMethod();
        System.out.println("请求方式是："+method);
        String protocol = request.getProtocol();
        System.out.println("协议类型为："+protocol);
        File file = new File(staticDir,uri);

        /*如果请求页面不存在，则返回404*/
        int statusCode;  //状态码
        String statusReason;  //状态描述
        if(file.isFile()) {
                /*statusCode = 200;
                statusReason = "OK";*/

            /*由于网络应用中，大多数请求都回应200ok，则在HttpServlet中将属性初始化为200，"ok"即可*/
                /*response.setStatusCode(200);
                response.setStatusReason("OK");*/
            response.setContentFile(file);
        }else {
                /*statusCode = 404;
                statusReason = "NotFound";*/
            file = new File(staticDir,"404.html");
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            response.setContentFile(file);
        }
    }
    public static DispatchServlet getInstance(){
        return instance;
    }
}
