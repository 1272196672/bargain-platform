package com.lzx.service;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author 林子翔
 * @since 2022 09 2022/9/28
 */
public interface WxPayService {
    /**
     * nativePay
     *
     * @param orderNum orderNum
     * @return {@link HashMap }<{@link String }, {@link Object }> 返回二维码与订单号
     * @author 林子翔
     * @since 2022/09/29
     */
    HashMap<String, Object> nativePay(String orderNum) throws Exception;

    /**
     * 处理订单
     *
     * @param bodyMap wx返回的参数
     * @author 林子翔
     * @since 2022/09/29
     */
    void processOrder(HashMap<String, Object> bodyMap) throws Exception;

    /**
     * 取消订单
     *
     * @param orderNum 订单号
     * @author 林子翔
     * @since 2022/09/29
     */
    void cancelOrder(String orderNum) throws Exception;

    /**
     * 调用wx客户端查单接口
     *
     * @param orderNum 订单号
     * @return {@link HashMap }<{@link String }, {@link String }>
     * @author 林子翔
     * @since 2022/10/09
     */
    HashMap<String, String> queryOrder(String orderNum) throws Exception;

    /**
     * 根据订单号查询wx支付查单接口，核实订单状态
     * 如果订单已支付，更新商户端订单状态
     * 如果订单确实未支付，调用wx端关单接口关单，更新商户端订单状态
     *
     * @param orderNo 订单号
     * @author 林子翔
     * @since 2022/09/30
     */
    void checkPayOrderStatus(String orderNo) throws Exception;

    /**
     * 申请退款
     *
     * @param orderNum 订单编号
     * @param reason         原因
     * @author 林子翔
     * @since 2022/09/30
     */
    void refund(String orderNum, String reason) throws IOException;

    /**
     * 查询退款单
     *
     * @param refundNo 退款单编号
     * @return {@link String }
     * @author 林子翔
     * @since 2022/09/30
     */
    String queryRefund(String refundNo) throws Exception;

    /**
     * 处理退款单
     *
     * @param bodyMap wx返回的参数
     * @author 林子翔
     * @since 2022/09/30
     */
    void processRefund(HashMap<String, Object> bodyMap) throws Exception;
}
