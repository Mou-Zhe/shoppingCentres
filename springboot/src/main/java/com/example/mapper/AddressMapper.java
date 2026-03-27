package com.example.mapper;
import com.example.entity.Address;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AddressMapper {


    int insert(Address address);

    void updateById(Address address);

    void deleteById(Integer id);

    @Select("select * from `address` where id = #{id}")
    Address selectById(Integer id);
    

    List<Address> selectAll(Address address);


}
