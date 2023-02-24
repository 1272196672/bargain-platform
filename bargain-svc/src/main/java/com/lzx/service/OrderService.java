package com.lzx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzx.entity.Order;
import com.lzx.enums.OrderStatus;

import java.util.List;

/**
 * <p>
 * 共享停车订单表 服务类
 * </p>
 *
 * @author LiJunXiao
 * @since 2022-03-08
 */
public interface OrderService extends IService<Order> {
    /**
     * 获取订单支付状态
     *
     * @param orderNo 订单id
     * @return short
     * @author 林子翔
     * @since 2022/10/08
     */
    int getOrderPayStatus(String orderNo);


    /**
     * 更新订单状态
     *
     * @param orderNo     订单id
     * @param orderStatus 成功
     * @author 林子翔
     * @since 2022/10/08
     */
    void updateStatusByOrderNo(String orderNo, OrderStatus orderStatus);

    /**
     * 查询创建时间超过 minute min的未支付订单
     *
     * @param minute 分钟
     * @return {@link List }<{@link Order }>
     * @author 林子翔
     * @since 2022/10/08
     */
    List<Order> getNoPayOrderByDuration(int minute);

    int getNeedPayMoneyByOrderNo(String orderNo);
}
