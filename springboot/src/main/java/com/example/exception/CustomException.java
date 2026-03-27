package com.example.exception;

import com.example.common.enums.ResultCodeEnum;

/**
 * 自定义异常类（修复后）
 */
public class CustomException extends RuntimeException {
    // 注意：code建议用Integer，与枚举的code类型一致（枚举中是int）
    private String code;
    private String msg;

    // 构造器1：自定义code和msg
    public CustomException(String code, String msg) {
        super(msg); // 关键：调用父类构造器，设置核心message
        this.code = code;
        this.msg = msg;
    }

    // 构造器2：通过枚举初始化（修复字段访问方式）
    public CustomException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMsg()); // 关键：将枚举msg传给父类
        this.code = String.valueOf(resultCodeEnum.getCode()); // 枚举code是int，转String
        this.msg = resultCodeEnum.getMsg();
    }

    // Getter（保留，Setter可删除，异常字段不应被修改）
    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
//package com.example.exception;
//
//import com.example.common.enums.ResultCodeEnum;
//
//public class CustomException extends RuntimeException {//自定义异常类，也会被全局异常类捕获的子类
//    private String code;
//    private String msg;
//
//    public CustomException(String code, String msg) {//自定义异常信息的构造函数，个性化初始code和msg
//        this.code = code;
//        this.msg = msg;
//    }
//    public CustomException(ResultCodeEnum resultCodeEnum) {//自定义异常信息的构造函数，个性化初始code和msg
//        this.code = resultCodeEnum.code;
//        this.msg = resultCodeEnum.msg;
//    }
//
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
//}
