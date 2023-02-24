package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzx.entity.PayOrder;
import com.lzx.enums.OrderStatus;
import com.lzx.mapper.PayOrderMapper;
import com.lzx.service.PayOrderService;
import com.lzx.util.OrderNoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 林子翔
 * @since 2022 10 2022/10/9
 */
@Service
@Slf4j
public class PayOrderServiceImpl implements PayOrderService {
    @Autowired
    private PayOrderMapper payOrderMapper;

    @Override
    public String getAndCreateOrderNo(String orderNum) {
        PayOrder payOrder = createPayOrderByOrderNum(orderNum);
        return payOrder.getOrderNum();
    }

    @Override
    public String getOrderNum(String orderNo) {
        QueryWrapper<PayOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_num", orderNo);
        return payOrderMapper.selectOne(queryWrapper).getOrderNum();
    }

    @Override
    public List<String> getNoPayOrderNosByOrderNum(String orderNum) {
        QueryWrapper<PayOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_num", orderNum)
                .eq("order_pay_status", 0);
        ArrayList<String> orderNoLists = new ArrayList<>();
        for (PayOrder payOrder : payOrderMapper.selectList(queryWrapper)) {
            orderNoLists.add(payOrder.getOrderNum());
        }
        return orderNoLists;
    }

    @Override
    public List<String> getAllOrderNoByOrderNum(String orderNum) {
        QueryWrapper<PayOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_num", orderNum);
        ArrayList<String> orderNoLists = new ArrayList<>();
//        循环遍历所有支付订单映射，找出其orderNo
        for (PayOrder payOrder : payOrderMapper.selectList(queryWrapper)) {
            orderNoLists.add(payOrder.getOrderNum());
        }
        return orderNoLists;
    }

    @Override
    public void updateStatusByOrderNo(String orderNo, OrderStatus orderStatus) {
        log.info("将支付订单映射状态更新为：{}", orderStatus.getType());

        QueryWrapper<PayOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_num", orderNo);

        PayOrder payOrder = new PayOrder();
        payOrder.setOrderPayStatus(orderStatus.getType());
        
        payOrderMapper.update(payOrder, queryWrapper);
    }

    @Override
    public String getPayOrderNoByOrderNum(String orderNum) {
        QueryWrapper<PayOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_num", orderNum)
                .eq("order_pay_status", 1);
        return payOrderMapper.selectOne(queryWrapper).getOrderNum();
    }

    /**
     * 创建订单映射
     *
     * @param orderNum 原始订单编号
     * @return {@link PayOrder }
     * @author 林子翔
     * @since 2022/10/09
     */
    public PayOrder createPayOrderByOrderNum(String orderNum) {
        PayOrder payOrder = new PayOrder();
        payOrder.setOrderNum(orderNum);
        payOrder.setOrderNum(OrderNoUtils.getOrderNo());
        payOrder.setOrderPayStatus(0);
        payOrderMapper.insert(payOrder);
        return payOrder;
    }
}
