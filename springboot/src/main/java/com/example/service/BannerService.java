package com.example.service;

import com.example.entity.Banner;
import com.example.mapper.BannerMapper;
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
public class BannerService {

    @Resource
    private BannerMapper bannerMapper;


    //1.新增，参数为对象
    public void add(Banner banner) {
        bannerMapper.insert(banner);
    }

    //2.查询所有
    public List<Banner> selectAll(Banner banner) {
        return bannerMapper.selectAll(banner);
    }

    //3.根据id查询
    public Banner selectById(Integer id) {
        return bannerMapper.selectById(id);
    }

    //4.分页查询的方法
    public PageInfo<Banner> selectPage(Banner banner,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Banner> list=bannerMapper.selectAll(banner);
        return PageInfo.of(list);
    }

    //5.根据id修改
    public void updateById(Banner banner) {
        bannerMapper.updateById(banner);
    }


    //6.根据ID删除
    public void deleteById(Integer id) {
        bannerMapper.deleteById(id);
    }


    //7.批量删除
    public void deleteBatch(@RequestBody List<Integer> ids) {
        for (Integer id:ids){
            bannerMapper.deleteById(id);
        }
    }



}
