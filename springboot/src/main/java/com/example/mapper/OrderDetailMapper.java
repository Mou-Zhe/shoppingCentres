

package com.example.mapper;
import com.example.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {


    int insert(OrderDetail orderDetail);

    void updateById(OrderDetail orderDetail);

    void deleteById(Integer id);

    @Select("select * from `order_detail` where id = #{id}")
    OrderDetail selectById(Integer id);


    List<OrderDetail> selectAll(OrderDetail orderDetail);

    @Select("select * from `order_detail` where order_id = #{orderId}")
    List<OrderDetail> selectByOrderId(Integer orderId);
}
