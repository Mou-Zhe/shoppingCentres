package com.example.common.config;

import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.service.AdminService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j; // 增加日志，方便排查
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT拦截器（最终修复版）,使用HMAC256算法
 */
@Slf4j // 日志注解，必须添加lombok依赖
@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Resource
    private AdminService adminService;

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // ========== 1. 获取Token（请求头/参数） ==========
        String token = request.getHeader(Constants.TOKEN);
        log.info("【1/5】从请求头获取Token：{}", token);

        if (ObjectUtil.isNull(token)) {
            token = request.getParameter(Constants.TOKEN);
            log.info("【1/5】从请求参数获取Token：{}", token);
        }

        // Token为空直接报错
        if (ObjectUtil.isNull(token)) {
            log.error("【1/5】Token为空，拒绝访问");
            throw new CustomException(ResultCodeEnum.TOKEN_INVALID_ERROR);
        }

        // ========== 2. 解析Token中的用户信息 ==========
        User userAccount=null;
        Admin adminAccount = null; // 修正：用Admin而非Account
        String userId = null;
        String role = null;
        try {
            // 解析Audience（userId-role）
            String audience = JWT.decode(token).getAudience().get(0);
            log.info("【2/5】解析到Audience：{}", audience);

            String[] audienceArr = audience.split("-");
            // 校验数组长度
            if (audienceArr.length != 2) {
                log.error("【2/5】Audience格式错误，应为userId-role");
                throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
            }

            userId = audienceArr[0];
            role = audienceArr[1];
            log.info("【2/5】解析成功 - userId：{}，role：{}", userId, role);

            // 校验角色非空
            if (ObjectUtil.isNull(role)) {
                log.error("【2/5】Token中的角色为空");
                throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
            }

            // ========== 3. 根据角色查询用户（忽略大小写） ==========
            String adminRole = RoleEnum.ADMIN.name();
            String userRole = RoleEnum.USER.name();
            if (adminRole.equalsIgnoreCase(role)) { // 核心修复：忽略大小写
                log.info("【3/5】匹配ADMIN角色，查询用户ID：{}", userId);
                adminAccount = adminService.selectById(Integer.valueOf(userId));
            } else if(userRole.equalsIgnoreCase(role)){
                log.info("【3/5】匹配USER角色，查询用户ID：{}", userId);
                userAccount = userService.selectById(Integer.valueOf(userId));
            }else {
                log.error("【3/5】角色不匹配，实际：{}", role);
                throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
            }
        } catch (NumberFormatException e) {
            log.error("【2/5】用户ID不是数字：{}", userId, e);
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        } catch (Exception e) {
            log.error("【2/5】解析Token失败", e);
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }

//        // ========== 4. 验证用户是否存在 ==========
//        if (ObjectUtil.isNull(account)) {
//            log.error("【4/5】用户不存在 - userId：{}", userId);
//            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
//        }
//        log.info("【4/5】查询到用户：{}", account.getUsername());

//        // ========== 5. 验证Token签名 ==========
//        try {
//            log.info("【5/5】开始验证Token签名");
//            Algorithm algorithm = Algorithm.HMAC256(account.getPassword());
//            JWTVerifier jwtVerifier = JWT.require(algorithm)
//                    .acceptExpiresAt(30) // 允许30秒时间误差
//                    .build();
//            jwtVerifier.verify(token); // 验证签名+过期时间
//            log.info("【5/5】Token签名验证成功，放行请求");
//        } catch (JWTVerificationException e) {
//            log.error("【5/5】Token签名验证失败", e);
//            // 区分过期和签名错误
//            if (e.getMessage().contains("expired")) {
//                throw new CustomException(ResultCodeEnum.TOKEN_EXPIRED_ERROR);
//            } else {
//                throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
//            }
//        }

        return true;
    }
}

//package com.example.common.config;
//
//import cn.hutool.core.util.ObjectUtil;
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.exceptions.JWTVerificationException;
//import com.example.common.Constans;
//import com.example.common.enums.ResultCodeEnum;
//import com.example.common.enums.RoleEnum;
//import com.example.entity.Account;
//import com.example.exception.CustomException;
//import com.example.service.AdminService;
//import jakarta.annotation.Resource;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
///**
// *JWT拦截器
// */
//
//@Component
//public class JWTInterceptor implements HandlerInterceptor {
//
//    @Resource
//    private AdminService adminService;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        //1.从HTTP请求中获取token
//        String token=request.getHeader(Constans.TOKEN);
//        if (ObjectUtil.isNull(token)) {
//            //如果没有拿到，那么再从请求中获取一次
//            request.getParameter(Constans.TOKEN);
//        }
//        //2.开始执行认证
//        if (ObjectUtil.isNull(token)){
//            throw new CustomException(ResultCodeEnum.TOKEN_INVALID_ERROR);//token无效情况
//        }
//        //根据用户角色判断用户属于哪个数据库表，然后查询用户数据
//        Account account=null;
//        try {
//            String audience= JWT.decode(token).getAudience().get(0);
//            String userId=audience.split("-")[0];
//            String role=audience.split("-")[1];
//            if(RoleEnum.ADMIN.name().equals(role)){
//                account= adminService.selectById(Integer.valueOf(userId));
//            }
//        }catch(Exception e){
//                throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
//        }
//        //根据token里面携带的用户ID去对应的角色表查询，没查到于是报了这个‘用户不存在’错误
//        if(ObjectUtil.isNull(account)){
//            //用户不存在
//            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
//        }
//        //通过用户的密码作为密钥再次验证token的合法性
//        try {
//            JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256(account.getPassword())).build();
//            jwtVerifier.verify(token);//验证token
//        }catch (JWTVerificationException e) {
//            //用户不存在
//            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
//        }
//        return true;
//    }
//
//}
