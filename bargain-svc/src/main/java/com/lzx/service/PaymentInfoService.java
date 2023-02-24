package com.lzx.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lzx.entity.PaymentInfo;

import java.util.HashMap;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 林子翔
 * @since 2022-09-28
 */
public interface PaymentInfoService extends IService<PaymentInfo> {

    /**
     * 记录支付日志
     *
     * @param plainTextMap 解密后的明文map
     * @author 林子翔
     * @since 2022/09/29
     */
    void createPaymentInfo(HashMap plainTextMap);

    /**
     * 获取支付金额
     *
     * @param orderNo orderNo
     * @return int
     * @author 林子翔
     * @since 2022/10/09
     */
    int getPayedMoney(String orderNo);
}
