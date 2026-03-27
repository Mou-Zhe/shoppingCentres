package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Collect;
import com.example.mapper.CollectMapper;
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
public class CollectService {

    @Resource
    private CollectMapper collectMapper;



    public void add(Collect collect) {
        Collect c=collectMapper.selectByUserIdAndGoodsId(collect.getUserId(),collect.getGoodsId());
        if(c!=null){
            collectMapper.deleteById(c.getId());
        }else {
            collect.setTime(DateUtil.now());
            collectMapper.insert(collect);
        }
    }

    public List<Collect> selectAll(Collect collect) {
        return collectMapper.selectAll(collect);
    }

    public Collect selectById(Integer id) {
        return collectMapper.selectById(id);
    }

    //分页查询的方法
    public PageInfo<Collect> selectPage(Collect collect,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Collect> list=collectMapper.selectAll(collect);
        return PageInfo.of(list);
    }

    public void updateById(Collect collect) {
        collectMapper.updateById(collect);
    }

    public void deleteById(Integer id) {
        collectMapper.deleteById(id);
    }

    public void deleteBatch(@RequestBody List<Integer> ids) {
        for (Integer id:ids){
            collectMapper.deleteById(id);
        }
    }



}
