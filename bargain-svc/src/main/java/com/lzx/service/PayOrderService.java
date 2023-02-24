package com.lzx.service;


import com.lzx.enums.OrderStatus;

import java.util.List;

/**
 * @author 林子翔
 * @since 2022 10 2022/10/9
 */
public interface PayOrderService {

    /**
     * 创建支付订单映射，获取订单编号映射
     *
     * @param orderNum 原始订单编号
     * @return {@link String }
     * @author 林子翔
     * @since 2022/10/09
     */
    String getAndCreateOrderNo(String orderNum);

    /**
     * 获取原始订单编号
     *
     * @param orderNo 映射编号
     * @return {@link String }
     * @author 林子翔
     * @since 2022/10/09
     */
    String getOrderNum(String orderNo);

    /**
     * 获取未支付的订单映射
     *
     * @param orderNum orderNum
     * @return {@link String }
     * @author 林子翔
     * @since 2022/10/09
     */
    List<String> getNoPayOrderNosByOrderNum(String orderNum);

    /**
     * 订单编号
     *
     * @param orderNum orderNum
     * @return {@link List }<{@link String }>
     * @author 林子翔
     * @since 2022/10/09
     */
    List<String> getAllOrderNoByOrderNum(String orderNum);

    /**
     * 更新订单状态
     *
     * @param orderNo     orderNo
     * @param orderStatus orderStatus
     * @author 林子翔
     * @since 2022/10/09
     */
    void updateStatusByOrderNo(String orderNo, OrderStatus orderStatus);
    

    /**
     * @param orderNum orderNum
     * @return {@link String }
     * @author 林子翔
     * @since 2022/10/09
     */
    String getPayOrderNoByOrderNum(String orderNum);
}
