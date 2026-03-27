package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Address;
import com.example.mapper.AddressMapper;
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
public class AddressService {

    @Resource
    private AddressMapper addressMapper;


    public void add(Address address) {
        addressMapper.insert(address);
    }

    public List<Address> selectAll(Address address) {
        return addressMapper.selectAll(address);
    }

    public Address selectById(Integer id) {
        return addressMapper.selectById(id);
    }

    //分页查询的方法
    public PageInfo<Address> selectPage(Address address,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Address> list=addressMapper.selectAll(address);
        return PageInfo.of(list);
    }

    public void updateById(Address address) {
        addressMapper.updateById(address);
    }

    public void deleteById(Integer id) {
        addressMapper.deleteById(id);
    }

    public void deleteBatch(@RequestBody List<Integer> ids) {
        for (Integer id:ids){
            addressMapper.deleteById(id);
        }
    }



}
