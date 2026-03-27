package com.example.mapper;
import com.example.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrdersMapper {


    int insert(Orders order);

    void updateById(Orders order);

    void deleteById(Integer id);

    @Select("select * from `order` where id = #{id}")
    Orders selectById(Integer id);
    

    List<Orders> selectAll(Orders order);


}
