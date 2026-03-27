package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Category;
import com.example.mapper.CategoryMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * 业务层方法
 */
@Service
public class CategoryService {

    @Resource
    private CategoryMapper categoryMapper;


    //1.新增，参数为对象
    public void add(Category category) {
        categoryMapper.insert(category);
    }

    //2.查询所有
    public List<Category> selectAll(Category category) {
        return categoryMapper.selectAll(category);
    }

    //3.根据id查询
    public Category selectById(Integer id) {
        return categoryMapper.selectById(id);
    }

    //4.分页查询的方法
    public PageInfo<Category> selectPage(Category category,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Category> list=categoryMapper.selectAll(category);
        return PageInfo.of(list);
    }

    //5.根据id修改
    public void updateById(Category category) {
        categoryMapper.updateById(category);
    }


    //6.根据ID删除
    public void deleteById(Integer id) {
        categoryMapper.deleteById(id);
    }


    //7.批量删除
    public void deleteBatch(@RequestBody List<Integer> ids) {
        for (Integer id:ids){
            categoryMapper.deleteById(id);
        }
    }



}
