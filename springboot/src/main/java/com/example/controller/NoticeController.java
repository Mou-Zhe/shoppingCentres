package com.example.controller;

import com.example.common.Result;
import com.example.entity.Notice;
import com.example.service.NoticeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 前端请求接口
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource//调用前引入资源
    private NoticeService noticeService;

    /**
     * 新增
     */
    @PostMapping("/add")//设置给vue调用的接口
    public Result add(@RequestBody Notice notice){
        //Controller调用servers,servers调用mapper
        noticeService.add(notice);
        // 修复：返回带code和msg的统一格式
        return Result.success("新增成功");
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Notice notice){
        noticeService.updateById(notice);
        return Result.success("修改成功");
    }

    /**
     * 删除单条
     */
    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id){ // 修复：方法名避免冲突
        noticeService.deleteById(id);
        return Result.success("删除成功");
    }

    /**
     * 批量删除（修复：参数名冲突问题）
     */
    @PostMapping("/batch/delete")
    public Result deleteBatch(@RequestBody List<Integer> ids){ // 修复：方法名改为deleteBatch
        noticeService.deleteBatch(ids);
        return Result.success("批量删除成功");
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Notice notice) {
        List<Notice> list = noticeService.selectAll(notice);
        return Result.success(list);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Notice notice = noticeService.selectById(id);
        return Result.success(notice);
    }

    /**
     * 分页查询（核心修复）
     */
    @GetMapping("/selectPage")
    public Result selectPage(Notice notice,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             // 修复：默认pageSize和前端一致
                             @RequestParam(defaultValue = "5") Integer pageSize) {
        // 修复：打印正确的查询条件（title而非username）
        System.out.println("接收的分页参数：pageNum=" + pageNum + ", pageSize=" + pageSize);
        System.out.println("接收的查询条件：title=" + (notice != null ? notice.getTitle() : "null"));

        PageInfo<Notice> pageInfo = noticeService.selectPage(notice, pageNum, pageSize);
        System.out.println("查询结果数量：" + pageInfo.getList().size());
        // 修复：返回PageInfo对象，前端可直接获取list和total
        return Result.success(pageInfo);
    }
}