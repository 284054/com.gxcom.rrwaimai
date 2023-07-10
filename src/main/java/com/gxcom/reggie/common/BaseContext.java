package com.gxcom.reggie.common;


/**
 * 基于ThreadLocal封装工具类、用户保存和获取当前登录用户id
 */

public class BaseContext {
    //登录用户id为Long
    private static ThreadLocal<Long> threadLocal = new InheritableThreadLocal<>();
    //工具方法,设置值
    public static void setCurrentId(Long id){
        //set方法设置进去
        threadLocal.set(id);
    }
    //获取值
    public static Long getCurrentId(){
        //get方法取出来
        return threadLocal.get();
    }
}
