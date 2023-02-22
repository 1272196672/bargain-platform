package com.lzx.service.impl;

import com.ptone.park.modules.share.enums.OrderStatus;

import java.util.List;

/**
 * @author 林子翔
 * @since 2022 10 2022/10/9
 */
public interface PayOrderService {

    /**
     * 创建支付订单映射，获取订单编号映射
     *
     * @param orderOriginNum 原始订单编号
     * @return {@link String }
     * @author 林子翔
     * @since 2022/10/09
     */
    String getAndCreateOrderNo(String orderOriginNum);

    /**
     * 获取原始订单编号
     *
     * @param orderNo 映射编号
     * @return {@link String }
     * @author 林子翔
     * @since 2022/10/09
     */
    String getOrderOriginNum(String orderNo);

    /**
     * 获取未支付的订单映射
     *
     * @param orderOriginNum orderOriginNum
     * @return {@link String }
     * @author 林子翔
     * @since 2022/10/09
     */
    List<String> getNoPayOrderNosByOriginNum(String orderOriginNum);

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
     * 获取所有原始订单的相关订单编号映射
     *
     * @param orderOriginNum orderOriginNum
     * @return {@link List }<{@link String }>
     * @author 林子翔
     * @since 2022/10/09
     */
    List<String> getAllOrderNoByOriginNum(String orderOriginNum);

    /**
     * @param orderOriginNum orderOriginNum
     * @return {@link String }
     * @author 林子翔
     * @since 2022/10/09
     */
    String getPayOrderNoByOriginNum(String orderOriginNum);
}
