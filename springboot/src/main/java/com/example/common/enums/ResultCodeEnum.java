package com.example.common.enums;

/**
 * 结果码枚举类（规范版）
 * 作用：统一返回码和提示信息，提高代码可维护性
 */
public enum ResultCodeEnum {
    // 通用状态码
    SUCCESS("200", "成功"),
    PARAM_ERROR("400", "参数异常"),
    SYSTEM_ERROR("500", "系统异常"),

    // Token相关
    TOKEN_INVALID_ERROR("401", "无效的token"),
    TOKEN_CHECK_ERROR("4000", "token验证失败，请重新登录"),
    TOKEN_LOST_ERROR("4001", "token参数缺失"),

    // 用户相关
    USER_EXIST_ERROR("5001", "用户名已存在"),
    USER_NOT_LOGIN("5002", "用户未登录"),
    USER_ACCOUNT_ERROR("5003", "账号或者密码错误"),
    USER_NOT_EXIST_ERROR("5004", "用户不存在"),
    PARAM_PASSWORD_ERROR("5005", "原密码输入错误"),
    TOKEN_EXPIRED_ERROR("5006","Token签名验证失败" );

    // 枚举字段（private + getter，符合封装规范）
    private final String code;
    private final String msg;

    // 唯一有效的构造器（用于初始化枚举项）
    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    // 必须添加getter方法（供CustomException调用）
    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
//package com.example.common.enums;
//
//public enum ResultCodeEnum {//枚举类，用于构造枚举参数去规范参数数据，提高代码修改效率
//    SUCCESS("200","成功"),
//
//    PARAM_ERROR("400","参数异常"),
//
//    TOKEN_INVALID_ERROR("401","无效的token,401"),
//
//    TOKEN_CHECK_ERROR("4000","token验证失败，请重新登录,4000"),
//
//    TOKEN_LOST_ERROR("4001","参数缺失4001"),
//
//    SYSTEM_ERROR("500","系统异常500"),
//
//    USER_EXIST_ERROR("5001","用户名已存在"),
//
//    USER_NOT_LOGIN("5002","用户未登录"),
//
//    USER_ACCOUNT_ERROR("5003","账号或者密码错误"),
//
//    USER_NOT_EXIST_ERROR("5004","用户不存在"),
//
//    PARAM_PASSWORD_ERROR("5005","原密码输入错误"),
//    ;
//    public String  code;
//    public String msg;
//
//    ResultCodeEnum(String code, String msg) {
//        this.code = code;
//        this.msg = msg;
//    }
//
//    ResultCodeEnum(ResultCodeEnum resultCodeEnum) {
//        this.code = resultCodeEnum.code;
//        this.msg = resultCodeEnum.msg;
//    }
//
//
//}
