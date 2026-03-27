package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.exception.CustomException;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import com.example.mapper.AdminMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


/**
 * 业务层方法
 */
@Service
public class AdminService {

    @Resource
    private AdminMapper adminMapper;



    public void add(Admin admin) {
        //先查询数据库
        Admin dbAdmin=adminMapper.selectByUsername(admin.getUsername());
        if(ObjectUtil.isNotNull(dbAdmin)){
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        admin.setRole(RoleEnum.ADMIN.name());
        if(ObjectUtil.isEmpty(admin.getPassword())){
            admin.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if(ObjectUtil.isEmpty(admin.getName())){
            admin.setPassword(admin.getUsername());
        }
        //再根据要求增加
        adminMapper.insert(admin);
    }

    public List<Admin> selectAll(Admin admin) {
        return adminMapper.selectAll(admin);
    }

    public Admin selectById(Integer id) {
        return adminMapper.selectById(id);
    }

    //分页查询的方法
    public PageInfo<Admin> selectPage(Admin admin,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Admin> list=adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }





    public void updateById(Admin admin) {
        adminMapper.updateById(admin);
    }

    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    public void deleteBatch(@RequestBody List<Integer> ids) {
        for (Integer id:ids){
            adminMapper.deleteById(id);
        }
    }

//    登录方法

    // AdminService的login方法（最终版）
    public Admin login(Account account) {
        Admin dbAdmin = adminMapper.selectByUsername(account.getUsername());
// 打印数据库查询的原始值
        System.out.println("dbAdmin.getId() = " + dbAdmin.getId()); // 应输出1
        System.out.println("dbAdmin.getRole() = " + dbAdmin.getRole()); // 修复后应输出ADMIN

// 兜底生成Token
        if (ObjectUtil.isNull(dbAdmin)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!dbAdmin.getPassword().equals(account.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        String token = TokenUtils.createToken(dbAdmin.getId() + "-" + dbAdmin.getRole(), dbAdmin.getPassword());
        System.out.println("生成Token的Audience：" + dbAdmin.getId() + "-" + dbAdmin.getRole()); // 应输出1-ADMIN
//        // 生成Token：Audience格式为 "userId-ADMIN"，就是这段开始错误
//        String token = TokenUtils.createToken(dbAdmin.getId() + "-" + dbAdmin.getRole(), dbAdmin.getPassword());

        // 打印验证（确认生成的Audience正确）
        System.out.println("生成Token的Audience：" + dbAdmin.getId() + "-" +  dbAdmin.getRole());
        dbAdmin.setToken(token);
        return dbAdmin;
    }

    public void updatePassword(Account account) {
        Admin dbAdmin = adminMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbAdmin)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if(!account.getPassword().equals(dbAdmin.getPassword())){
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        dbAdmin.setPassword(account.getNewPassword());
        adminMapper.updateById(dbAdmin);
    }

}
