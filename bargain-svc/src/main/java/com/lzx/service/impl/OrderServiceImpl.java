package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzx.entity.Order;
import com.lzx.enums.OrderStatus;
import com.lzx.mapper.OrderMapper;
import com.lzx.service.OrderService;
import com.lzx.util.OrderNoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;

/**
 * <p>
 * 共享停车订单表 服务实现类
 * </p>
 *
 * @author LiJunXiao
 * @since 2022-03-08
 */
@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Override
    public int getOrderPayStatus(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_num", orderNo);
        Order order = orderMapper.selectOne(queryWrapper);
        return order.getOrderStatus();
    }

    @Override
    public void updateStatusByOrderNo(String orderNo, OrderStatus orderStatus) {
        log.info("将订单状态更新为：{}", orderStatus.getType());

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_num", orderNo);

        Order order = new Order();
        order.setOrderStatus(orderStatus.getType());

        orderMapper.update(order, queryWrapper);
    }

    @Override
    public List<Order> getNoPayOrderByDuration(int minute) {
//        得到minute分钟之前的时间
        Instant instant = Instant.now().minus(Duration.ofMinutes(minute));

        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_status", OrderStatus.NOTPAY.getType())
                .le("create_time", instant);

        return orderMapper.selectList(queryWrapper);
    }

    @Override
    public int getNeedPayMoneyByOrderNo(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_num", orderNo);
        BigDecimal money = orderMapper.selectOne(queryWrapper).getRealMoney();
        return money.multiply(new BigDecimal(100)).intValue();
    }

    @Override
    public String getAndCreateOrderNo(String orderNum) {
        Order order = createOrderByOrderNum(orderNum);
        return order.getOrderNum();
    }

    @Override
    public List<String> getNoPayOrderNosByOrderNum(String orderNum) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_num", orderNum)
                .eq("order_status", 0);
        ArrayList<String> orderNoLists = new ArrayList<>();
        for (Order order : orderMapper.selectList(queryWrapper)) {
            orderNoLists.add(order.getOrderNum());
        }
        return orderNoLists;
    }

    /**
     * 创建订单映射
     *
     * @param orderNum 原始订单编号
     * @return {@link Order }
     * @author 林子翔
     * @since 2022/10/09
     */
    public Order createOrderByOrderNum(String orderNum) {
        Order order = new Order();
        order.setOrderNum(orderNum);
        order.setOrderNum(OrderNoUtils.getOrderNo());
        order.setOrderStatus(OrderStatus.NOTPAY.getType());
        orderMapper.insert(order);
        return order;
    }
}
