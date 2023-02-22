package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ptone.common.base.vo.Result;
import com.ptone.park.modules.share.dto.PlaceCarPortNumDTO;
import com.ptone.park.modules.share.dto.ShareParkOrderDTO;
import com.ptone.park.modules.share.dto.ShareParkOrderMoneyDTO;
import com.ptone.park.modules.share.dto.ShareParkOrderViolationDTO;
import com.ptone.park.modules.share.entity.ShareParkOrder;
import com.ptone.park.modules.share.enums.OrderStatus;
import com.ptone.park.modules.share.form.*;
import com.ptone.park.modules.sys.form.BlackOrderForm;

import java.util.List;

/**
 * <p>
 * 共享停车订单表 服务类
 * </p>
 *
 * @author LiJunXiao
 * @since 2022-03-08
 */
public interface OrderService extends IService<ShareParkOrder> {
    /**
     * 提醒用户到达预约时间
     *
     * @return
     */
    boolean remindUserReserveTime();

    /**
     * 把所有超出预约时间15分钟的订单给自动关闭同时设置金额
     *
     * @return
     */
    boolean updateAllUserOutTimeOrder();


    List<Long> getAllOrderPlace();

    /**
     * 改变订单的超时状态
     *
     * @return
     */
    boolean updateOutStatus();

    /**
     * 获取所有临近预约时间的订单对用户进行提示
     *
     * @return
     */
    boolean remindUserNearReserveTime();


    /**
     * 提醒所有快到将要超时停车的用户
     *
     * @return
     */
    boolean remindUserNearOutTime();


    List<ShareParkOrderDTO> getNearOutTimeList(ShareParkOrderQueryForm form);


    /**
     * 取消订单
     *
     * @param order
     * @return
     */
    boolean cancelOrder(ShareParkOrder order);


    /**
     * 初始化生成订单
     *
     * @param form 前端传递的数据对象
     * @return 初始化是否成功
     */
    boolean initOrderAndOrderInfo(ShareParkOrderInsertForm form);

    /**
     * 获取用户所有未完成的订单
     *
     * @param userId 用户
     * @return
     */
    List<ShareParkOrder> getNoFinishedOrderByUserId(Long userId);


    /**
     * 获取车牌号未完成的订单
     *
     * @param carNum 车牌号
     * @return
     */
    List<ShareParkOrder> getNoFinishedOrderByCarNum(String carNum);

    /**
     * 根据表单检查用户及车位及场所是否满足下单条件
     *
     * @param form 表单
     * @return
     */
    Result checkAllInfoBeforeOrderInit(ShareParkOrderInsertForm form);


    /**
     * 多条件查询订单包装类信息
     *
     * @param form
     * @return
     */
    List<ShareParkOrderDTO> getShareParkOrderList(ShareParkOrderQueryForm form);

    /**
     * 多条件分页
     *
     * @param form
     */
    IPage<ShareParkOrderDTO> getShareParkOrderPageList(ShareParkOrderQueryForm form);

    /**
     * @param form
     * @return
     */
    boolean getCarInAndOutInfo(ShareParkOrderOtherForm form);

    /**
     * 获取订单详细信息
     *
     * @param form
     * @return
     */
    ShareParkOrderDTO getOrderAllInfoByOrderNum(ShareParkOrderQueryForm form);

    /**
     * 获取订单对象用于修改
     *
     * @param form
     * @return
     */
    ShareParkOrder getOne(ShareParkOrderQueryForm form);

    /**
     * 获取订单对象用于修改
     *
     * @param orderNum
     * @return
     */
    ShareParkOrder getOne(String orderNum);

    /**
     * 修改订单
     *
     * @param order
     * @return
     */
    boolean updateShareParkOrder(ShareParkOrder order);

    /**
     * 计算系统金额、超时金额
     *
     * @param form
     * @return
     */
    ShareParkOrderMoneyDTO getOrderCount(ShareParkOrderQueryForm form);


    Integer countOrderOutTimeByOrderNum(String orderNum);

    /**
     * 结算金额
     *
     * @param form
     * @return
     */
    boolean accountsOrderCount(ShareParkOrderCountForm form);


    /**
     * 计算超时分钟
     *
     * @param dto
     * @param t
     * @return
     */
    ShareParkOrderDTO countOutTimeMinute(ShareParkOrderDTO dto, boolean t);

    /**
     * 白名单列表
     *
     * @return
     */
    List<String> getWhiteCarNum();


    /**
     * 生成线下订单同时打开道闸
     *
     * @param form
     * @return
     */
    boolean recordInfoAndOpenRoadGate(OfflineOrderInsertForm form);

    /**
     * 生成对应的车位数情况
     *
     * @param placeId
     * @return
     */
    PlaceCarPortNumDTO getPlaceCarPortInfo(Long placeId);

    /**
     * 检测根据情况自动下单
     *
     * @return
     */
    boolean checkAutoOrder();

    /**
     * 查询违约订单
     *
     * @param form
     * @return
     */
    IPage<ShareParkOrderViolationDTO> getOutOrderList(BlackOrderForm form);

    /**
     * 定时任务提醒用户超时时间
     *
     * @return
     */
    boolean remindUserOutTime();

    /**
     * 获取订单支付状态
     *
     * @param orderNo 订单id
     * @return short
     * @author 林子翔
     * @since 2022/10/08
     */
    int getOrderPayStatus(String orderNo);


    /**
     * 更新订单状态
     *
     * @param orderNo     订单id
     * @param orderStatus 成功
     * @author 林子翔
     * @since 2022/10/08
     */
    void updateStatusByOrderNo(String orderNo, OrderStatus orderStatus);

    /**
     * 查询创建时间超过 minute min的未支付订单
     *
     * @param minute 分钟
     * @return {@link List }<{@link ShareParkOrder }>
     * @author 林子翔
     * @since 2022/10/08
     */
    List<ShareParkOrder> getNoPayOrderByDuration(int minute);

    int getNeedPayMoneyByOrderNo(String orderNo);
}
