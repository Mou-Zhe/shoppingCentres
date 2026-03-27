package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;


/**
 * 业务层方法
 */
@Service
public class UserService {

    @Resource
    private UserMapper userMapper;



    public void add(User user) {
        //先查询数据库
        User dbUser=userMapper.selectByUsername(user.getUsername());
        if(ObjectUtil.isNotNull(dbUser)){
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        user.setRole(RoleEnum.USER.name());
        if(ObjectUtil.isEmpty(user.getPassword())){//如果密码为空，设置默认密码
            user.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        if(ObjectUtil.isEmpty(user.getName())){
            user.setPassword(user.getUsername());
        }
        //再根据要求增加
        userMapper.insert(user);
    }

    public List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }

    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    //分页查询的方法
    public PageInfo<User> selectPage(User user,Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<User> list=userMapper.selectAll(user);
        return PageInfo.of(list);
    }





    public void updateById(User user) {
        userMapper.updateById(user);
    }


    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    public void deleteBatch(@RequestBody List<Integer> ids) {
        for (Integer id:ids){
            userMapper.deleteById(id);
        }
    }

//    登录方法

    // UserService的login方法（最终版）
    public User login(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!dbUser.getPassword().equals(account.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }

        // 生成Token：Audience格式为 "userId-ADMIN"
        String token = TokenUtils.createToken(dbUser.getId() + "-" + dbUser.getRole(), dbUser.getPassword());

        // 打印验证（确认生成的Audience正确）
        System.out.println("生成Token的Audience：" + dbUser.getId() + "-" + dbUser.getRole());
        dbUser.setToken(token);
        return dbUser;
    }



    public void updateAccount(BigDecimal account) {
        Account currentUser=TokenUtils.getCurrentUser();
        userMapper.updateAccount(currentUser.getId(),account);
    }

    public void updatePassword(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if(!account.getPassword().equals(dbUser.getPassword())){
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        dbUser.setPassword(account.getNewPassword());
        userMapper.updateById(dbUser);
    }
//    public User login(Account account) {
//        User dbUser=userMapper.selectByUsername(account.getUsername());
//        if(ObjectUtil.isNull(dbUser)){
//            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
//        }
//        if(!dbUser.getPassword().equals(account.getPassword())){
//            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
//        }
//        //生成 Token
//        String token=TokenUtils.createToken(dbUser.getId()+"-"+dbUser.getRole(),dbUser.getPassword());
//        dbUser.setToken(token);
//        return dbUser;//返回前端
//    }
}
