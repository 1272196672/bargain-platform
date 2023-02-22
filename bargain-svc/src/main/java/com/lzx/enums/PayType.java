package com.lzx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 林子翔
 * @since 2022 09 2022/9/28
 */
@Getter
@AllArgsConstructor
public enum PayType {
    /**
     * @description 微信
     * @since 2022/09/28
     */
    WXPAY("微信"),

    /**
     * @description 支付宝
     * @since 2022/09/28
     */
    ALPAY("支付宝");

    /**
     * @description 类型
     * @since 2022/09/28
     */
    private final String type;
}
