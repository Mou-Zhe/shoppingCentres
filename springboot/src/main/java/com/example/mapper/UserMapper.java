package com.example.mapper;
import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface UserMapper {


    int insert(User user);

    void updateById(User user);

    void deleteById(Integer id);

    @Select("select * from `user` where id = #{id}")
    User selectById(Integer id);

    @Select("select * from `user` where username = #{username}")
    User selectByUsername(String username);


    List<User> selectAll(User user);

    @Update("update `user` set account=account +#{account} where id=#{id}")
    void updateAccount(@Param("id")Integer id, @Param("account") BigDecimal account);


//
//    //mybatis数据的两种写法，静态使用注解方式，动态字符使用xml，没有就不比对，直接跳过到下一个字段的修改
//   int insert(User user);// UserMapper.xml通过头文件映射到此java文件,Int类型返回索引值
//    @Select("select * from user where username=#{username}")
//    User selectByUsername(@Param("username") String username);
//
//
//    List<User> selectAll(User user);
}
