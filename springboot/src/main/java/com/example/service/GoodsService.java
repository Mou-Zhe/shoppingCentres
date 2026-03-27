package com.example.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Collect;
import com.example.entity.Goods;
import com.example.mapper.CollectMapper;
import com.example.mapper.GoodsMapper;
import com.example.utils.TokenUtils;
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
public class GoodsService {

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private CollectMapper collectMapper;


    //1.新增，参数为对象
    public void add(Goods goods) {
        goods.setSaleCount(480);
        goods.setViewCount(1350);
        goods.setDate(DateUtil.today());
        goodsMapper.insert(goods);
    }

    //2.查询所有
    public List<Goods> selectAll(Goods goods) {
        return goodsMapper.selectAll(goods);
    }

    //3.根据id查询
    public Goods selectById(Integer id) {
       Goods goods= goodsMapper.selectById(id);
        Account currentUser=TokenUtils.getCurrentUser();
        if(currentUser!=null){
            Collect collect=collectMapper.selectByUserIdAndGoodsId(currentUser.getId(),id);
            goods.setUserCollect(collect!=null);
        }

        return goods;
    }

    //4.分页查询的方法
    public PageInfo<Goods> selectPage(Goods goods,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Goods> list=goodsMapper.selectAll(goods);
        return PageInfo.of(list);
    }

    //5.根据id修改
    public void updateById(Goods goods) {
        goodsMapper.updateById(goods);
    }


    //6.根据ID删除
    public void deleteById(Integer id) {
        goodsMapper.deleteById(id);
    }


    //7.批量删除
    public void deleteBatch(@RequestBody List<Integer> ids) {
        for (Integer id:ids){
            goodsMapper.deleteById(id);
        }
    }



}
