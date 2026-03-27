package com.example.controller;

import com.example.common.Result;
import com.example.common.enums.ResultCodeEnum;
import com.example.exception.CustomException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

//供前端连接的test和hello两个接口
@RequestMapping("/test")
@RestController

public class TestController {
    @GetMapping("/hello")
    public Result hello(){
        throw new CustomException(ResultCodeEnum.PARAM_ERROR);//构造函数类型错误，去枚举类中建立同类型构造函数
    }
}
