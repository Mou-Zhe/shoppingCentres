package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.example.entity.*;
import com.example.exception.CustomException;
import com.example.mapper.OrderDetailMapper;
import com.example.mapper.OrdersMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * 业务层方法
 */
@Service
public class OrdersService {

    @Resource
    private AddressService addressService;
    @Resource
    private OrderDetailMapper  orderDetailMapper;
    @Resource
    private GoodsService goodsService;
    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    private CartService cartService;
    @Resource
    private UserService userService;

    @Transactional  //代码抛异常 → 数据库操作自动撤销立即回滚，具有原子性，电商、支付、购物车、订单的add必须加，否则会出大问题
    public void add(Integer addressId, List<Cart> cartList) {
        Address address = addressService.selectById(addressId);
        if(address==null){
            throw new CustomException("500","收货地址不存在");
        }
        Account currentUser = TokenUtils.getCurrentUser();
        Orders orders=new Orders();
        orders.setAddressId(addressId);
        orders.setUserId(currentUser.getId());
        orders.setTime(DateUtil.now());
        String orderNo= IdUtil.getSnowflakeNextIdStr();//订单唯一性编号
        orders.setOrderNo(orderNo);
        orders.setStatus("待支付");

        BigDecimal totalAmount=BigDecimal.ZERO;
        String name="";
        Integer totalNum=0;
        List<OrderDetail> orderDetailList=new ArrayList<>();
        for (Cart cart : cartList) {
            Integer num = cart.getNum();
            totalNum+=num;
            Integer goodsId = cart.getGoodsId();
            String goodsName = cart.getGoodsName();
            Goods goods = goodsService.selectById(goodsId);
            if(goods==null){
                throw new CustomException("500","商品"+goodsName+"已不存在");
            }

            name+=goods.getName()+"、";

            Integer store=goods.getStore();
            if(store<num){
                throw new CustomException("500","商品库存不足");
            }
            goods.setStore(goods.getStore()-num);
            goodsService.updateById(goods);

            BigDecimal price=goods.getPrice();
            totalAmount= totalAmount.add(price.multiply(BigDecimal.valueOf(num)).setScale(2, BigDecimal.ROUND_HALF_UP));

            OrderDetail orderDetail=new OrderDetail();
            orderDetail.setNum(num);
            orderDetail.setPrice(price);
            orderDetail.setGoodsId(goodsId);
            orderDetail.setGoodsName(goods.getName());
            orderDetail.setGoodsImg(goods.getImg());
            orderDetailMapper.insert(orderDetail);
            orderDetailList.add(orderDetail);

            //删除购物车数据
            cartService.deleteById(cart.getId());
        }
        name=name.substring(0,name.length()-1);//删除最末尾多余的、
        name+="等"+totalNum+"件商品";
        orders.setName(name);
        orders.setTotal(totalAmount);
        ordersMapper.insert(orders);//xml文件中，id关联

        //更新订单详情里面的订单ID
        Integer orderId=orders.getId();
        for (OrderDetail orderDetail : orderDetailList) {
            orderDetail.setOrderId(orderId);
            orderDetailMapper.insert(orderDetail);
        }
    }

    public List<Orders> selectAll(Orders order) {
        return ordersMapper.selectAll(order);
    }

    public Orders selectById(Integer id) {
        return ordersMapper.selectById(id);
    }

    //分页查询的方法
    public PageInfo<Orders> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Orders> list=ordersMapper.selectAll(orders);
        for (Orders o : list) {//循环获取订单详细数据
            Integer orderId=o.getId();
            List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(orderId);
            o.setOrderDetailList(orderDetails);
        }
        return PageInfo.of(list);
    }

    @Transactional
    public void updateById(Orders order) {
        if(order.getStatus().equals("已取消")){
            List<OrderDetail> orderDetailList=orderDetailMapper.selectByOrderId(order.getId());
            for (OrderDetail orderDetail : orderDetailList) {
                Integer goodsId=orderDetail.getGoodsId();
                Goods goods=goodsService.selectById(goodsId);
                if(goods!=null){
                    goods.setStore(orderDetail.getNum()+goods.getStore());
                    goodsService.updateById(goods);
                }
            }
        }
        if(order.getStatus().equals("已完成")){
            List<OrderDetail> orderDetailList=orderDetailMapper.selectByOrderId(order.getId());
            for (OrderDetail orderDetail : orderDetailList) {
                Integer goodsId=orderDetail.getGoodsId();
                Goods goods=goodsService.selectById(goodsId);
                if(goods!=null){
                    goods.setSaleCount(orderDetail.getNum()+goods.getSaleCount());
                    goodsService.updateById(goods);
                }
            }
        }
        ordersMapper.updateById(order);
    }

    public void deleteById(Integer id) {
        ordersMapper.deleteById(id);
    }

    public void deleteBatch(@RequestBody List<Integer> ids) {
        for (Integer id:ids){
            ordersMapper.deleteById(id);
        }
    }

//    @Transactional
//    public void pay(Integer orderId) {
//        Account currentUser = TokenUtils.getCurrentUser();
//        User user=userService.selectById(currentUser.getId());
//        Orders orders = ordersMapper.selectById(orderId);
//        BigDecimal account=user.getAccount();
//        if(account.compareTo(orders.getTotal())<0){
//            throw new CustomException("500","对不起，您的余额不足，需要充值");
//        }
//        user.setAccount(user.getAccount().subtract(orders.getTotal()));
//        userService.updateById(user);
//        orders.setStatus("待发货");
//        ordersMapper.updateById(orders);
//    }
@Transactional
public void pay(Integer orderId) {
    Account currentUser = TokenUtils.getCurrentUser();
    User user = userService.selectById(currentUser.getId());
    Orders orders = ordersMapper.selectById(orderId);

    // ========== bug修复操作：依次判空 ==========
    if (orders == null) {
        throw new CustomException("500", "订单不存在");
    }
    if (orders.getTotal() == null) {
        throw new CustomException("500", "订单金额异常");
    }
    if (user.getAccount() == null) {
        throw new CustomException("500", "用户余额未初始化");
    }

    BigDecimal account = user.getAccount();
    if (account.compareTo(orders.getTotal()) < 0) {
        throw new CustomException("500", "余额不足，请充值");
    }

    user.setAccount(user.getAccount().subtract(orders.getTotal()));
    userService.updateById(user);

    orders.setStatus("待发货");
    ordersMapper.updateById(orders);
}

    public  List<Orders> selectByDate(String dateStr){
        return ordersMapper.selectByDate(dateStr);
    }
}
