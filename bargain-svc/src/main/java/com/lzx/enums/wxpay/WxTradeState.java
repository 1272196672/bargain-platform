package com.lzx.enums.wxpay;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 林子翔
 * @description 商户与微信支付平台的支付交易状态
 * @since 2022/09/28
 */
@AllArgsConstructor
@Getter
public enum WxTradeState {

    /**
     * 支付成功
     */
    SUCCESS("SUCCESS"),

    /**
     * 未支付
     */
    NOTPAY("NOTPAY"),

    /**
     * 已关闭
     */
    CLOSED("CLOSED"),

    /**
     * 转入退款
     */
    REFUND("REFUND");

    /**
     * 类型
     */
    private final String type;
}
