package com.lzx.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ptone.park.modules.share.dto.ShareParkOrderDTO;
import com.ptone.park.modules.share.entity.ShareParkOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 共享停车订单表 Mapper 接口
 * </p>
 *
 * @author LiJunXiao
 * @since 2022-03-08
 */
public interface ShareParkOrderMapper extends BaseMapper<ShareParkOrder> {
    /**
     * 获取用户所有未结束的订单
     * @param userId 用户id
     * @return
     */
    List<ShareParkOrder> getNoFinishedOrderByUserId(Long userId);


    List<ShareParkOrder> getNoFinishedOrderByCarNum(String carNum);

    /**
     * 多条件分页列表
     * @param wrapper
     * @return
     */
    List<List<ShareParkOrderDTO>> getPageOrderList(@Param(Constants.WRAPPER) QueryWrapper<ShareParkOrder> wrapper);


    /**
     * 多条件订单列表
     * @param wrapper
     * @return
     */
    List<ShareParkOrderDTO> getOrderList(@Param(Constants.WRAPPER) QueryWrapper<ShareParkOrder> wrapper);


    List<Long> getAllOrderPlaceId();

    List<List<?>> getOutOrderList(@Param(Constants.WRAPPER) QueryWrapper<ShareParkOrder> wrapper);
}
