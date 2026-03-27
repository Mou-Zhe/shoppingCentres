package com.example.entity;

import java.math.BigDecimal;

public class User extends Account{
    /*主键ID*/
    private Integer id;
//    账号
    private String username;
//    密码
    private String Password;
//    用户名
    private String name;
//    头像
    private String avatar;
//    角色
    private String role;
//    电话
    private String phone;
//    邮箱
    private String email;
    //余额
    private BigDecimal account;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getAccount() {return account;}

    public void setAccount(BigDecimal account) {this.account = account;}

}
//alt+inset快捷键getset