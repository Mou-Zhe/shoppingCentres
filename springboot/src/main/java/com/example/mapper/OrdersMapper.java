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

    @Select("select * from `orders` where id = #{id}")
    Orders selectById(Integer id);
    

    List<Orders> selectAll(Orders order);

//    已经收货的才统计售卖情况，以免退货导致数据有误
    @Select("select * from `orders` where time like concat(#{dateStr},'%') and status='已完成'")
    List<Orders> selectByDate(String dateStr);
}
