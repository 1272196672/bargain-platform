package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.lzx.entity.PaymentInfo;
import com.lzx.enums.PayType;
import com.lzx.mapper.PaymentInfoMapper;
import com.lzx.service.PaymentInfoService;
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
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {
    @Resource
    private PaymentInfoMapper paymentInfoMapper;

    @Override
    public void createPaymentInfo(HashMap plainTextMap) {
        log.info("记录支付日志");

        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setOrderNo((String) plainTextMap.get("out_trade_no"));
//        支付类型
        paymentInfo.setPaymentType(PayType.WXPAY.getType());
//        wx记录的支付的唯一编号
        paymentInfo.setTransactionId((String) plainTextMap.get("transaction_id"));
//        交易类型
        paymentInfo.setTradeType((String) plainTextMap.get("trade_type"));
//        交易状态
        paymentInfo.setTradeState((String) plainTextMap.get("trade_state"));
//        用户实际的支付金额
        Map amount = ((Map) plainTextMap.get("amount"));
        BigDecimal payerTotal = new BigDecimal(amount.get("payer_total").toString());
        paymentInfo.setPayerTotal(payerTotal);
//        全部其他消息
        paymentInfo.setContent(new Gson().toJson(plainTextMap));

        paymentInfoMapper.insert(paymentInfo);
    }

    @Override
    public int getPayedMoney(String orderNo) {
        QueryWrapper<PaymentInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);

        return paymentInfoMapper.selectOne(queryWrapper).getPayerTotal().intValue();
    }
}
