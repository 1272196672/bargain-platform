package com.lzx.service.impl;

import com.ptone.park.modules.share.entity.ShareParkRefundInfo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 林子翔
 * @since 2022-09-28
 */
public interface RefundInfoService {

    /**
     * 创建退款单记录
     *
     * @param orderNo 订单号
     * @param reason  原因
     * @return {@link ShareParkRefundInfo }
     * @author 林子翔
     * @since 2022/09/30
     */
    ShareParkRefundInfo createRefundByOrderNo(String orderNo, String reason);

    /**
     * 更新退款单
     *
     * @param bodyAsString wx端返回的参数
     * @author 林子翔
     * @since 2022/09/30
     */
    void updateRefund(String bodyAsString);
}
