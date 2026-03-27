package com.example.controller;


import com.example.common.Result;
import com.example.entity.User;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource//调用前引入资源
    private UserService userService;

    /**
     * 新增
     */
    @PostMapping("/add")//设置给vue调用的接口
    public Result add(@RequestBody User user){
        //Controller调用servers,servers调用mapper
        userService.add(user);
        return Result.success();
    }

/**
 * 修改
 */
    @PutMapping("/update")
    public Result update(@RequestBody User user){
        userService.updateById(user);
        return Result.success();
    }

    @PutMapping("/updateAccount/{account}")
    public Result update(@PathVariable BigDecimal account){
        userService.updateAccount(account);
        return Result.success();
    }

    /**
     * 删除单条
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        userService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @PostMapping("/batch/delete")
    public Result delete(@RequestBody List<Integer> ids){
        userService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(User user) {
        List<User> list = userService.selectAll(user);
        return Result.success(list);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        User user = userService.selectById(id);
        return Result.success(user);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(User user,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        // 增加日志：打印接收的参数，排查是否传值成功
        System.out.println("接收的分页参数：pageNum=" + pageNum + ", pageSize=" + pageSize);
        System.out.println("接收的查询条件：username=" + (user != null ? user.getUsername() : "null"));

        PageInfo<User> pageInfo = userService.selectPage(user, pageNum, pageSize);
        // 打印查询结果数量
        System.out.println("查询结果数量：" + pageInfo.getList().size());
        return Result.success(pageInfo);
    }
}
