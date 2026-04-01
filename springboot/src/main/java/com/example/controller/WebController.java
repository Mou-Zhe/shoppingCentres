package com.example.controller;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.common.Result;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Orders;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.OrderDetailMapper;
import com.example.service.AdminService;
import com.example.service.OrdersService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
public class WebController {

    @Resource
    private AdminService adminService;

    @Resource
    private UserService userService;

    @Resource
    OrderController orderController;

    @Resource
    OrderDetailMapper orderDetailMapper;
    @Autowired
    private CompositeMeterRegistryAutoConfiguration compositeMeterRegistryAutoConfiguration;
    @Autowired
    private OrdersService ordersService;

    /*
    **
    默认请求接口用于测试
    */
    @GetMapping("/")
    public Result hello(){
        return Result.success();
    }


    @GetMapping("/selectLine")
    public Result selectLine(){
        //返回横轴的日期的数据,今天的数据往回推30天
        Date today = new Date();
        DateTime startDate = DateUtil.offsetDay(today, -30);
        List<DateTime> dateTimeList = DateUtil.rangeToList(startDate, today, DateField.DAY_OF_YEAR);
        List<String> dateStrList = dateTimeList.stream().map(DateUtil::formatDate).toList();
        List<BigDecimal>countList=new ArrayList<>();
        for (String dateStr : dateStrList) {
            List<Orders> ordersList = ordersService.selectByDate(dateStr);
//            java8的stream流
            BigDecimal total = ordersList.stream().map(Orders::getTotal).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            countList.add(total);
        }
        Map<String, Object> map = Map.of(
                "date",dateStrList,
                "count",countList
        );

        return Result.success(map);
    }

    /*
    **
    登录接口
    */
    @PostMapping("/login")
    public Result login(@RequestBody Account account){
        Account loginAccount=null;
        if(RoleEnum.ADMIN.name().equals(account.getRole())){
            loginAccount=adminService.login(account);
        }else if(RoleEnum.USER.name().equals(account.getRole())){
            loginAccount=userService.login(account);
        }
        return Result.success(loginAccount);
    }




    /**
     * 修改密码
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Account account){
        try {
            // 1. 校验入参非空
            if (account == null || account.getRole() == null || account.getUsername() == null) {
                return Result.error("参数不能为空");
            }
            // 2. 按角色处理
            if(RoleEnum.ADMIN.name().equals(account.getRole())){
                adminService.updatePassword(account);
            } else if(RoleEnum.USER.name().equals(account.getRole())){
                userService.updatePassword(account);
            } else {
                return Result.error("角色类型错误");
            }
            // 3. 只有无异常才返回成功
            return Result.success("密码修改成功");
        } catch (CustomException e) {
            // 捕获业务异常（原密码错误、用户不存在）
            return Result.error(e.getMessage()); // 返回到前端的msg是具体错误
        } catch (Exception e) {
            // 捕获系统异常
            e.printStackTrace();
            return Result.error("系统异常，修改失败");
        }
    }
//    /**
//     * 修改密码
//     */
//    @PutMapping ("/updatePassword")
//    public Result updatePassword(@RequestBody Account account){
//        if(RoleEnum.ADMIN.name().equals(account.getRole())){
//            adminService.updatePassword(account);
//        }else  if(RoleEnum.USER.name().equals(account.getRole())){
//            userService.updatePassword(account);
//        }else {
//            return  Result.error("出现问题");
//        }
//        return Result.success();
//    }
    /*
    **
    注册接口
    */
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        userService.add(user);
        return Result.success("请求成功");
    }




}
