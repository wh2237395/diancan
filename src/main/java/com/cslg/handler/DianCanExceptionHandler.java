package com.cslg.handler;

import com.cslg.exception.DianCanAuthorizeException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 编程小石头：2501902696（微信）
 */
@ControllerAdvice
public class DianCanExceptionHandler {


    //拦截登录异常
    //http://localhost:8080/diancan/leimu/list
    @ExceptionHandler(value = DianCanAuthorizeException.class)
    public String handlerAuthorizeException() {
        return "zujian/loginView";
    }
}
