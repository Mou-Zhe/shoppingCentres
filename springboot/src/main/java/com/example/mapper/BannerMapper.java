package com.example.mapper;
import com.example.entity.Banner;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BannerMapper {


    int insert(Banner banner);

    void updateById(Banner banner);

    void deleteById(Integer id);

    @Select("select * from `banner` where id = #{id}")
    Banner selectById(Integer id);
    

    List<Banner> selectAll(Banner banner);


}
