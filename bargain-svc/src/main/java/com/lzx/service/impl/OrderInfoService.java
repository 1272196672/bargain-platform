package com.lzx.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ptone.park.modules.share.entity.ShareParkOrderInfo;

/**
 * @author 林子翔
 * @since 2022/10/8
 */
public interface OrderInfoService extends IService<ShareParkOrderInfo> {

    /**
     * 根据订单id查询车牌号
     *
     * @param orderNo 订单编号
     * @return {@link String }
     * @author 林子翔
     * @since 2022/10/08
     */
    String getCarNumByOrderNo(String orderNo);
}
