package com.gxcom.reggie.common;

/**
 * 自定义业务异常
 * RuntimeException运行时异常
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }

}
