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
     * @param orderOriginNum orderOriginNum
     * @return {@link HashMap }<{@link String }, {@link Object }> 返回二维码与订单号
     * @author 林子翔
     * @since 2022/09/29
     */
    HashMap<String, Object> nativePay(String orderOriginNum) throws Exception;

    /**
     * h5Pay
     *
     * @param orderOriginNum orderOriginNum
     * @param ip             手机ip地址
     * @param phoneTye       手机类型
     * @return {@link HashMap }<{@link String }, {@link Object }>
     * @author 林子翔
     * @since 2022/10/08
     */
    HashMap<String, Object> h5Pay(String orderOriginNum, String ip, String phoneTye) throws Exception;

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
     * @param orderOriginNum 订单号
     * @author 林子翔
     * @since 2022/09/29
     */
    void cancelOrder(String orderOriginNum) throws Exception;

    /**
     * 调用wx客户端查单接口
     *
     * @param orderOriginNum 订单号
     * @return {@link HashMap }<{@link String }, {@link String }>
     * @author 林子翔
     * @since 2022/10/09
     */
    HashMap<String, String> queryOrder(String orderOriginNum) throws Exception;

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
     * @param orderOriginNum 订单编号
     * @param reason         原因
     * @author 林子翔
     * @since 2022/09/30
     */
    void refund(String orderOriginNum, String reason) throws IOException;

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
