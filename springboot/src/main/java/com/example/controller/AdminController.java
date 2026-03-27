package com.example.controller;


import com.example.common.Result;
import com.example.entity.Admin;
import com.example.service.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.naming.factory.ResourceLinkFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource//调用前引入资源
    private AdminService adminService;

    /**
     * 新增
     */
    @PostMapping("/add")//设置给vue调用的接口
    public Result add(@RequestBody Admin admin){
        //Controller调用servers,servers调用mapper
        adminService.add(admin);
        return Result.success();
    }

/**
 * 修改
 */
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin){
        adminService.updateById(admin);
        return Result.success();
    }

    /**
     * 删除单条
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        adminService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @PostMapping("/batch/delete")
    public Result delete(@RequestBody List<Integer> ids){
        adminService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Admin admin) {
        List<Admin> list = adminService.selectAll(admin);
        return Result.success(list);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Admin admin = adminService.selectById(id);
        return Result.success(admin);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Admin admin,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        // 增加日志：打印接收的参数，排查是否传值成功
        System.out.println("接收的分页参数：pageNum=" + pageNum + ", pageSize=" + pageSize);
        System.out.println("接收的查询条件：username=" + (admin != null ? admin.getUsername() : "null"));

        PageInfo<Admin> pageInfo = adminService.selectPage(admin, pageNum, pageSize);
        // 打印查询结果数量
        System.out.println("查询结果数量：" + pageInfo.getList().size());
        return Result.success(pageInfo);
    }
}
