package com.example.exception;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.example.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice("com.example.controller")//检测com.example.controller这个包的异常
public class GlobalExceptionHandler {//全局异常处理，捕获异常

    private  static final Log log= LogFactory.get();

    @ExceptionHandler(Exception.class)//捕获所有异常
    @ResponseBody//返回异常json串，返回到前台
    public Result error(Exception e){
        log.error("异常信息：",e);//用hutool插件，在debug控制台打印异常提示日志信息
        return Result.error();
    }

    @ExceptionHandler(CustomException.class)//捕获自定义的异常
    @ResponseBody//返回异常json串，返回到前台
    public Result error(CustomException e){
        log.error("异常信息：",e);//用hutool插件，在debug控制台打印异常提示日志信息
        return Result.error(e.getCode(),e.getMsg());
    }
}
