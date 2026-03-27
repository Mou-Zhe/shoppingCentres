package com.example.common;

/**
 * 统一返回结果类
 * 用于前后端交互的标准化响应格式
 */
public class Result {
    // 响应状态码（前端依赖此字段判断请求结果）
    private String code;
    // 响应提示信息
    private String msg;
    // 响应数据体
    private Object data;

    /**
     * 成功响应（无返回数据）
     * 仅告知前端请求成功，无业务数据返回
     */
    public static Result success() { // 修正原拼写错误 sucess -> success
        Result result = new Result();
        result.setCode("200");       // 必设：成功状态码
        result.setMsg("请求成功");
        result.setData(null);       // 显式设置null，语义更清晰
        return result;
    }

    /**
     * 成功响应（带返回数据）
     * 核心修复：补充设置 code = "200"，解决前端判断失效问题
     */
    public static Result success(Object data) {
        Result result = new Result();
        result.setCode("200");       // 关键修复：新增设置code，不再为null
        result.setMsg("请求成功");
        result.setData(data);        // 仅保留一次赋值，去除重复代码
        return result;
    }

    /**
     * 通用失败响应（系统异常）
     * 默认返回500状态码 + 系统异常提示
     */
    public static Result error() {
        Result result = new Result();
        result.setCode("500");       // 系统异常状态码
        result.setMsg("系统异常");
        result.setData(null);
        return result;
    }

    /**
     * 自定义失败响应（指定状态码和提示信息）
     * 适用于特殊业务异常场景
     */
    public static Result error(String code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    /**
     * 自定义失败响应（仅指定提示信息，默认500状态码）
     */
    public static Result error(String msg) {
        Result result = new Result();
        result.setCode("500");
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    // Getter & Setter 方法（保持不变）
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
//package com.example.common;
////Result用于返回数据
//public class Result {
//    private String code;//前端必须有code
//    private String msg;//信息
//    private Object data;//返回的数据
//
//    public  static  Result sucess(){
//        Result result=new Result();
//        result.setCode("200");
//        result.setMsg("请求成功");
//        return result;
////        此方法只是用于确定调用成功，不需要返回数据
//    }
//    public  static  Result success(Object data){
//        Result result=new Result();
//        result.setData(data);
//        result.setMsg("请求成功");
//        result.setData(data);
//        return result;
//    }
//
//    public  static  Result error(){
//        Result result=new Result();
//        result.setCode("500");
//        result.setMsg("系统异常");
//        return result;
////        此方法用于调用后台报错，提示字段500
//    }
//
//    public  static  Result error(String code,String msg){
//        Result result=new Result();
//        result.setCode(code);
//        result.setMsg(msg);
//        result.setData(null);
//        return result;//        此方法用于调用后台报错，主要用于考虑特殊场景，且用于个人指定的code和msg进行调试抛出异常
//    }
//
//    public  static  Result error(String msg){
//        Result result=new Result();
//        result.setCode("500");
//        result.setMsg(msg);
//        return result;//        此方法用于调用后台报错，主要用于考虑特殊场景，且用于个人指定的code和msg进行调试抛出异常
//    }
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public Object getData() {
//        return data;
//    }
//
//    public void setData(Object data) {
//        this.data = data;
//    }
//
//
//}
