package com.lzx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 林子翔
 * @description 用户与商户 之间的订单状态
 * @since 2022 09 2022/9/28
 */
@Getter
@AllArgsConstructor
public enum OrderStatus {
    /**
     * @description 未支付
     * @since 2022/09/28
     */
    NOTPAY(0),

    /**
     * @description 支付成功
     * @since 2022/09/28
     */
    SUCCESS(1),

    /**
     * @description 超时已关闭
     * @since 2022/09/28
     */
    CLOSED(2),

    /**
     * @description 用户已取消
     * @since 2022/09/28
     */
    CANCEL(3),

    /**
     * @description 退款中
     * @since 2022/09/28
     */
    REFUND_PROCESSING(4),

    /**
     * @description 退款成功
     * @since 2022/09/28
     */
    REFUND_SUCCESS(5),

    /**
     * @description 退款异常
     * @since 2022/09/28
     */
    REFUND_ABNORMAL(6);

    /**
     * @description 类型
     * @since 2022/09/28
     */
    private final int type;
}
