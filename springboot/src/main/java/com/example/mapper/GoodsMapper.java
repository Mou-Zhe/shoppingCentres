package com.example.mapper;
import com.example.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface GoodsMapper {


    int insert(Goods goods);

    void updateById(Goods goods);

    void deleteById(Integer id);

    @Select("select * from `goods` where id = #{id}")
    Goods selectById(Integer id);
    

    List<Goods> selectAll(Goods goods);


}
