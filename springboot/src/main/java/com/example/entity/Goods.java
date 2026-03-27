package com.example.entity;

import java.math.BigDecimal;

public class Goods {

    /**ID（主键，自增）*/
    private Integer id;

    /**商品名称*/
    private String name;

    /**商品编号*/
    private String no;

    /**商品描述*/
    private String description;

    /**商品图片地址*/
    private String img;

    /**商品价格（保留2位小数）*/
    private BigDecimal price;

    /**商品库存*/
    private Integer store;

    /**商品单位*/
    private String unit;

    /**分类ID（关联分类表主键）*/
    private Integer categoryId;

    /** 上架状态（可以用枚举：如 ON_SHELF-上架、OFF_SHELF-下架）*/
    private String status;

    /**销量*/
    private Integer saleCount;

    /**浏览量*/
    private Integer viewCount;

    /**日期*/
    private String date;

    /**商品分类*/
    private String categoryName;

    /**商品收藏信息*/
    private Boolean userCollect;

    public Boolean getUserCollect() {
        return userCollect;
    }

    public void setUserCollect(Boolean userCollect) {
        this.userCollect = userCollect;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Integer saleCount) {
        this.saleCount = saleCount;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}