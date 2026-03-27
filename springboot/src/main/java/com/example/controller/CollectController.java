package com.example.controller;

import com.example.common.Result;
import com.example.entity.Collect;
import com.example.service.CollectService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Delete;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/collect")
public class CollectController {

    @Resource//调用前引入资源
    private CollectService collectService;

    /**
     * 新增
     */
    @PostMapping("/add")//设置给vue调用的接口
    public Result add(@RequestBody Collect collect){
        //Controller调用servers,servers调用mapper
        collectService.add(collect);
        // 修复：返回带code和msg的统一格式
        return Result.success("新增成功");
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Collect collect){
        collectService.updateById(collect);
        return Result.success("修改成功");
    }

    /**
     * 删除单条
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id){ // 修复：方法名避免冲突
        collectService.deleteById(id);
        return Result.success("删除成功");
    }

    /**
     * 批量删除（修复：参数名冲突问题）
     */
    @DeleteMapping("/batch/delete")
    public Result deleteBatch(@RequestBody List<Integer> ids){ // 修复：方法名改为deleteBatch
        collectService.deleteBatch(ids);
        return Result.success("批量删除成功");
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Collect collect) {
        List<Collect> list = collectService.selectAll(collect);
        return Result.success(list);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Collect collect = collectService.selectById(id);
        return Result.success(collect);
    }

    /**
     * 分页查询（核心修复）
     */
    @GetMapping("/selectPage")
    public Result selectPage(Collect collect,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             // 修复：默认pageSize和前端一致
                             @RequestParam(defaultValue = "5") Integer pageSize) {
//        // 修复：打印正确的查询条件（title而非username）
//        System.out.println("接收的分页参数：pageNum=" + pageNum + ", pageSize=" + pageSize);
//        System.out.println("接收的查询条件：title=" + (collect != null ? collect.getTitle() : "null"));

        PageInfo<Collect> pageInfo = collectService.selectPage(collect, pageNum, pageSize);
        System.out.println("查询结果数量：" + pageInfo.getList().size());
        // 修复：返回PageInfo对象，前端可直接获取list和total
        return Result.success(pageInfo);
    }
}