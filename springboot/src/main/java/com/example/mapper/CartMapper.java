package com.example.mapper;

import com.example.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CartMapper {


    int insert(Cart cart);

    void updateById(Cart cart);

    void deleteById(Integer id);

    @Select("select * from `cart` where id = #{id}")
    Cart selectById(Integer id);
    

    List<Cart> selectAll(Cart cart);

//  修复：  表名的反引号写错为单引号
    @Select("select * from cart where user_id=#{userId} and goods_id = #{goodsId}")
    Cart selectByUserIdAndGoodsId(@Param("userId") Integer userId, @Param("goodsId") Integer goodsId);
}
