package com.lzx.enums.wxpay;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WxNotifyType {

    /**
     * native支付通知
     */
    NATIVE_NOTIFY("/api/wx-pay/native/notify"),

    /**
     * h5支付通知
     */
    H5_NOTIFY("/api/wx-pay/h5/notify"),

    /**
     * 退款结果通知
     */
    REFUND_NOTIFY("/api/wx-pay/refunds/notify");

    /**
     * 类型
     */
    private final String type;
}
