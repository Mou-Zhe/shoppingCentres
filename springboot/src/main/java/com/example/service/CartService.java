package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Cart;
import com.example.mapper.CartMapper;
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
public class CartService {

    @Resource
    private CartMapper cartMapper;



    public void add(Cart cart) {
        //对购物车进行同类合并
        Cart dbCart = cartMapper.selectByUserIdAndGoodsId(cart.getUserId(), cart.getGoodsId());
        if(dbCart != null) {
            dbCart.setNum(cart.getNum() + dbCart.getNum());
            cartMapper.updateById(dbCart);
        }else{
            cartMapper.insert(cart);
        }
    }

    public List<Cart> selectAll(Cart cart) {
        return cartMapper.selectAll(cart);
    }

    public Cart selectById(Integer id) {
        return cartMapper.selectById(id);
    }

    //分页查询的方法
    public PageInfo<Cart> selectPage(Cart cart,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Cart> list=cartMapper.selectAll(cart);
        return PageInfo.of(list);
    }

    public void updateById(Cart cart) {
        cartMapper.updateById(cart);
    }

    public void deleteById(Integer id) {
        cartMapper.deleteById(id);
    }

    public void deleteBatch(@RequestBody List<Integer> ids) {
        for (Integer id:ids){
            cartMapper.deleteById(id);
        }
    }



}
