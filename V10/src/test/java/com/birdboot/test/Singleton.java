package com.birdboot.test;
/*Java中有23种设计模式，每种模式可以解决一种固定的问题
单例模式
使用这个模式设计的类，全局只有一个实例
* */
public class Singleton {
    /*提供静态的当前类的属性并初始化（静态属性只初始化一次）*/
    private static Singleton instance = new Singleton();

    /*私有化当前类的构造器，杜绝外界随意实例化（new 对象）*/
    private Singleton(){}

    /*提供一个静态公开方法，可以返回当前类实例，返回的是类的属性*/
    public static Singleton getInstance(){
        return instance;
    }
}
