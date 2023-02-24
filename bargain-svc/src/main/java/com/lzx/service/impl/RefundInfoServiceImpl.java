package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.lzx.entity.Order;
import com.lzx.entity.PaymentInfo;
import com.lzx.entity.RefundInfo;
import com.lzx.mapper.OrderMapper;
import com.lzx.mapper.PaymentInfoMapper;
import com.lzx.mapper.RefundInfoMapper;
import com.lzx.service.PayOrderService;
import com.lzx.service.RefundInfoService;
import com.lzx.util.OrderNoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 林子翔
 * @since 2022-09-28
 */
@Service
@Slf4j
public class RefundInfoServiceImpl implements RefundInfoService {
    @Resource
    private OrderMapper orderMapper;

    @Autowired
    private PaymentInfoMapper paymentInfoMapper;

    @Autowired
    private RefundInfoMapper refundInfoMapper;

    @Autowired
    private PayOrderService payOrderService;

    @Override
    public RefundInfo createRefundByOrderNo(String orderNo, String reason) {

//        根据订单号获取订单消息
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
//        获取原订单编号
        String orderNum = payOrderService.getOrderNum(orderNo);

        queryWrapper.eq("order_num", orderNum);
        Order order = orderMapper.selectOne(queryWrapper);

//        从paymentInfo中获取用户支付金额
        QueryWrapper<PaymentInfo> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("order_no", orderNo);
        BigDecimal payerTotal = paymentInfoMapper.selectOne(queryWrapper1).getPayerTotal();

        //根据订单号生成退款订单
        RefundInfo refundInfo = new RefundInfo();
        refundInfo.setOrderNo(orderNo);
        refundInfo.setRefundNo(OrderNoUtils.getRefundNo());
        refundInfo.setTotalFee(payerTotal);
        refundInfo.setRefund(payerTotal);
        refundInfo.setReason(reason);

        //保存退款订单
        refundInfoMapper.insert(refundInfo);

        log.info(refundInfo.toString());

        return refundInfo;
    }

    @Override
    public void updateRefund(String bodyAsString) {
        //将json字符串转换成Map
        Map<String, String> bodyMap = new Gson().fromJson(bodyAsString, HashMap.class);

        //根据退款单编号修改退款单
        QueryWrapper<RefundInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("refund_no", bodyMap.get("out_refund_no"));

        //设置要修改的字段
        RefundInfo refundInfo = new RefundInfo();
        refundInfo.setRefundId(bodyMap.get("refund_id"));

        //查询退款和申请退款中的返回参数
//        申请退款
        if (bodyMap.get("status") != null) {
            refundInfo.setRefundStatus(bodyMap.get("status"));
            refundInfo.setContentReturn(bodyAsString);
        }

        //退款结果通知中回调中的回调参数
        if (bodyMap.get("refund_status") != null) {
            refundInfo.setRefundStatus(bodyMap.get("refund_status"));
            refundInfo.setContentNotify(bodyAsString);
        }

        //更新退款单
        refundInfoMapper.update(refundInfo, queryWrapper);
    }
}
