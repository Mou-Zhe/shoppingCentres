package com.example.entity;

public class Account {
    /*设置一些必要信息，作为父类集合供子类使用*/
    private Integer id;
    // 账号（正确：小写开头）
    private String username;
    // 密码（核心修正：首字母小写）
    private String password;
    // 修改后的新密码
    private String newPassword;
    // 角色（核心修正：首字母小写）
    private String role;
    // JWT 的前后端密钥（核心修正：首字母小写）
    private String token;

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    // 修正后的setter/getter（字段名全小写开头）
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password; // 对应小写password
    }

    public void setPassword(String password) {
        this.password = password; // 核心：this.小写字段名 = 参数
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRole() {
        return role; // 对应小写role
    }

    public void setRole(String role) {
        this.role = role; // 核心：this.小写字段名 = 参数
    }

    public String getToken() {
        return token; // 对应小写token
    }

    public void setToken(String token) {
        this.token = token; // 核心：this.小写字段名 = 参数
    }
}