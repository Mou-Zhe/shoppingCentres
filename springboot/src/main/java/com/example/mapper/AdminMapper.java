package com.example.mapper;
import com.example.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AdminMapper {


    int insert(Admin admin);

    void updateById(Admin admin);

    void deleteById(Integer id);

    @Select("select * from `admin` where id = #{id}")
    Admin selectById(Integer id);

//    @Select("select * from `admin` where username = #{username}")
//    Admin selectByUsername(String username);
      @Select("select * from `admin` where username = #{username}")
      Admin selectByUsername(String username);

    List<Admin> selectAll(Admin admin);

//
//    //mybatis数据的两种写法，静态使用注解方式，动态字符使用xml，没有就不比对，直接跳过到下一个字段的修改
//   int insert(Admin admin);// AdminMapper.xml通过头文件映射到此java文件,Int类型返回索引值
//    @Select("select * from admin where username=#{username}")
//    Admin selectByUsername(@Param("username") String username);
//
//
//    List<Admin> selectAll(Admin admin);
}
