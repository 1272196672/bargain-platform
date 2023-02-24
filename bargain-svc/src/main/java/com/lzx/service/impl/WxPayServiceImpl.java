package com.lzx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.lzx.config.WxPayConfig;
import com.lzx.entity.Order;
import com.lzx.entity.RefundInfo;
import com.lzx.enums.OrderStatus;
import com.lzx.enums.wxpay.WxApiType;
import com.lzx.enums.wxpay.WxNotifyType;
import com.lzx.enums.wxpay.WxTradeState;
import com.lzx.mapper.OrderMapper;
import com.lzx.service.*;
import com.lzx.service.WxPayService;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 林子翔
 * @since 2022 09 2022/9/28
 */
@Service
@Slf4j
public class WxPayServiceImpl implements WxPayService {
    @Autowired
    private WxPayConfig wxPayConfig;

    @Autowired
    private CloseableHttpClient wxPayClient;

    @Autowired
    private PaymentInfoService paymentInfoService;

    @Autowired
    private RefundInfoService refundInfoService;

    @Autowired
    private OrderService orderService;

    @Resource
    private OrderMapper orderMapper;

    @Autowired
    private PayOrderService payOrderService;

    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 调用wx native支付接口
     *
     * @param orderNum 原始订单编号
     * @return {@link HashMap }<{@link String }, {@link Object }> 返回二维码和订单号
     * @author 林子翔
     * @since 2022/09/28
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public HashMap<String, Object> nativePay(String orderNum) throws Exception {
        log.info("创建订单映射，获取订单映射num");
        String orderNo = payOrderService.getAndCreateOrderNo(orderNum);

        log.info("查询订单消息");
//        查询订单金额(分)
        int money = orderService.getNeedPayMoneyByOrderNo(orderNum);

        log.info("调用统一下单api");
//        调用统一下单api
        HttpPost httpPost = new HttpPost(wxPayConfig.getDomain().concat(WxApiType.NATIVE_PAY.getType()));

        // 请求body参数
        HashMap<Object, Object> paramsMap = new HashMap<>();
        paramsMap.put("appid", wxPayConfig.getAppId());
        paramsMap.put("mchid", wxPayConfig.getMchId());
        paramsMap.put("description", "商品描述：" + "");
        paramsMap.put("out_trade_no", orderNo);
        paramsMap.put("notify_url", wxPayConfig.getNotifyDomain().concat(WxNotifyType.NATIVE_NOTIFY.getType()));
        HashMap<Object, Object> amountMap = new HashMap<>();
        amountMap.put("total", money);
        amountMap.put("currency", "CNY");
        paramsMap.put("amount", amountMap);
        String jsonParams = new Gson().toJson(paramsMap);
        log.info("请求参数：" + jsonParams);

//        将请求参数放到请求对象中
        StringEntity entity = new StringEntity(jsonParams, "utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = wxPayClient.execute(httpPost);

        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            //处理成功
            if (statusCode == 200) {
                log.info("success,return body = " + bodyAsString);
                //处理成功，无返回Body
            } else if (statusCode == 204) {
                log.info("success");
            } else {
                log.error("failed,resp code = " + statusCode + ",return body = " + bodyAsString);
                throw new IOException("request failed");
            }

//            响应结果
            HashMap<String, String> responseMap = new Gson().fromJson(bodyAsString, HashMap.class);
//            解析出二维码
            String codeUrl = responseMap.get("code_url");
//            返回二维码
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("codeUrl", codeUrl);
            resultMap.put("orderId", orderNum);
            return resultMap;

        } finally {
            response.close();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processOrder(HashMap<String, Object> bodyMap) throws Exception {
        log.info("处理订单");

//        解密
        String plainText = decryptFromResource(bodyMap);
//        明文转换map
        HashMap plainTextMap = new Gson().fromJson(plainText, HashMap.class);
//        订单号
        String orderNum = (String) plainTextMap.get("out_trade_no");


        /*
        对业务数据进行 状态检查 和 处理 之前，
        使用ReentrantLock锁控制并发，
        避免函数重入导致数据混乱。
         */
//        尝试获取锁，成功获取进行业务操作；ReentrantLock获取失败不会一直等待，直接跳过
        if (lock.tryLock()) {
            try {
                //        处理重复通知,未支付的情况下继续
                int orderStatus = orderService.getOrderPayStatus(orderNum);
                if (OrderStatus.NOTPAY.getType() != orderStatus) {
                    return;
                }
                //        订单更新
                orderService.updateStatusByOrderNo(orderNum, OrderStatus.SUCCESS);
                System.out.println("订单详情：" + orderMapper.selectOne(new QueryWrapper<Order>().eq("order_num", orderNum)));
                //        支付订单映射更新
                payOrderService.updateStatusByOrderNo(orderNum, OrderStatus.SUCCESS);
                //        记录支付日志
                paymentInfoService.createPaymentInfo(plainTextMap);
            } finally {
//                主动释放锁
                lock.unlock();
            }
        }
    }

    /**
     * 取消订单
     *
     * @param orderNum orderNum
     * @author 林子翔
     * @since 2022/09/29
     */
    @Override
    public void cancelOrder(String orderNum) throws Exception {
//        获取orderNum的所有未支付订单编号
        List<String> orderNos = payOrderService.getNoPayOrderNosByOrderNum(orderNum);
        for (String orderNo : orderNos) {
            //        调用微信支付关单接口
            closeOrder(orderNo);
//        更新支付订单映射状态,设置为取消订单
            payOrderService.updateStatusByOrderNo(orderNo, OrderStatus.CANCEL);
//        更新商户端订单状态
            orderService.updateStatusByOrderNo(orderNum, OrderStatus.NOTPAY);
        }
    }

    @Override
    public HashMap<String, String> queryOrder(String orderNum) throws Exception {
        log.info("调用wx客户端查单接口");
        List<String> orderNoLists = payOrderService.getAllOrderNoByOrderNum(orderNum);
        log.info("{}有{}条记录", orderNum, orderNoLists.size());

        HashMap<String, String> bodyAsStringMap = new HashMap<>();

//        为每一条orderNo记录查询一次
        for (String orderNo : orderNoLists) {
            String url = wxPayConfig.getDomain()
                    .concat(String.format(WxApiType.ORDER_QUERY_BY_NO.getType(), orderNo))
                    .concat("?mchid=")
                    .concat(wxPayConfig.getMchId());

            //        将请求参数放到请求对象中
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/json");
            //        执行请求
            CloseableHttpResponse response = wxPayClient.execute(httpGet);

            try {
                String bodyAsString = EntityUtils.toString(response.getEntity());
                int statusCode = response.getStatusLine().getStatusCode();
                //处理成功
                if (statusCode == 200) {
                    log.info("success,return body = " + bodyAsString);
                    //处理成功，无返回Body
                } else if (statusCode == 204) {
                    log.info("success");
                } else {
                    log.error("failed,resp code = " + statusCode + ",return body = " + bodyAsString);
                    throw new IOException("request failed");
                }

                bodyAsStringMap.put(orderNo, bodyAsString);
            } finally {
                response.close();
            }
        }
        return bodyAsStringMap;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkPayOrderStatus(String orderNum) throws Exception {
        log.info("根据订单号{}核实订单状态", orderNum);
//        获取所有orderNum未支付的订单
        List<String> orderNos = payOrderService.getNoPayOrderNosByOrderNum(orderNum);
        for (String orderNo : orderNos) {
            //        调用wx支付查单接口
            String bodyAsString = this.queryOrder(orderNo).get(orderNo);

            HashMap bodyMap = new Gson().fromJson(bodyAsString, HashMap.class);

//        获取wx支付端订单状态
            Object tradeState = bodyMap.get("trade_state");
//        订单已支付
            if (WxTradeState.SUCCESS.getType().equals(tradeState)) {
                log.info("核实订单{}已支付", orderNo);

//            更新本地订单状态
                orderService.updateStatusByOrderNo(orderNum, OrderStatus.SUCCESS);

//            更新本地支付订单映射状态
                payOrderService.updateStatusByOrderNo(orderNo, OrderStatus.SUCCESS);

//            记录支付日志
                paymentInfoService.createPaymentInfo(bodyMap);
//            订单未支付
            } else if (WxTradeState.NOTPAY.getType().equals(tradeState)) {
                log.warn("核实订单{}未支付", orderNum);

//            调用wx端关单接口
                this.closeOrder(orderNo);

//            更新本地订单状态
                orderService.updateStatusByOrderNo(orderNum, OrderStatus.CLOSED);

//            更新本地支付订单映射状态
                payOrderService.updateStatusByOrderNo(orderNo, OrderStatus.CLOSED);
            }
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refund(String orderNum, String reason) throws IOException {
        log.info("创建退款单记录");

//        获取映射订单编号
        String orderNo = payOrderService.getPayOrderNoByOrderNum(orderNum);

        int money = paymentInfoService.getPayedMoney(orderNo);

        //根据订单编号创建退款单
        RefundInfo refundInfo = refundInfoService.createRefundByOrderNo(orderNo, reason);

//        调用wx退款api
        log.info("调用wx退款api");
        String url = wxPayConfig.getDomain().concat(WxApiType.DOMESTIC_REFUNDS.getType());
        HttpPost httpPost = new HttpPost(url);

        // 构造请求body参数
        Gson gson = new Gson();
        HashMap<String, Object> paramsMap = new HashMap();
        paramsMap.put("out_trade_no", orderNo);
        paramsMap.put("out_refund_no", refundInfo.getRefundNo());
        paramsMap.put("reason", reason);
        paramsMap.put("notify_url", wxPayConfig.getNotifyDomain().concat(WxNotifyType.REFUND_NOTIFY.getType()));
        HashMap<String, Object> amountMap = new HashMap();
        amountMap.put("refund", money);
        amountMap.put("total", money);
        amountMap.put("currency", "CNY");
        paramsMap.put("amount", amountMap);

        //将参数转换成json字符串
        String jsonParams = gson.toJson(paramsMap);
        log.info("请求参数:" + jsonParams);
        StringEntity entity = new StringEntity(jsonParams, "utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求，并完成验签
        CloseableHttpResponse response = wxPayClient.execute(httpPost);

        try {
            //解析响应结果
            String bodyAsString = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                log.info("success200, wx退款返回响应结果:{}", bodyAsString);
            } else if (statusCode == 204) {
                log.info("success204");
            } else {
                throw new RuntimeException("退款异常, 响应码 = " + statusCode + ", 退款返回结果 = " + bodyAsString);
            }

            //更新订单状态
            orderService.updateStatusByOrderNo(orderNum, OrderStatus.NOTPAY);

            //更新支付订单映射状态
            payOrderService.updateStatusByOrderNo(orderNo, OrderStatus.REFUND_PROCESSING);

            //更新退款单
            refundInfoService.updateRefund(bodyAsString);

        } finally {
            response.close();
        }
    }

    @Override
    public String queryRefund(String refundNo) throws Exception {

        log.info("查询退款单{}，退款接口调用", refundNo);

        String url = wxPayConfig.getDomain().concat(String.format(WxApiType.DOMESTIC_REFUNDS_QUERY.getType(), refundNo));

        //创建远程Get 请求对象
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = wxPayClient.execute(httpGet);

        try {
            String bodyAsString = EntityUtils.toString(response.getEntity());
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                log.info("成功200, 查询退款返回结果：{}", bodyAsString);
            } else if (statusCode == 204) {
                log.info("成功204");
            } else {
                throw new RuntimeException("查询退款异常, 响应码 = " + statusCode + ", 查询退款返回结果 = " + bodyAsString);
            }

            return bodyAsString;

        } finally {
            response.close();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processRefund(HashMap<String, Object> bodyMap) throws Exception {

        log.info("处理退款单");

        //解密报文
        String plainText = decryptFromResource(bodyMap);

        //将明文转换成map
        Gson gson = new Gson();
        HashMap plainTextMap = gson.fromJson(plainText, HashMap.class);
        String orderNo = (String) plainTextMap.get("out_trade_no");

        String orderNum = payOrderService.getOrderNum(orderNo);

        if (lock.tryLock()) {
            try {
                //更新订单状态
                orderService.updateStatusByOrderNo(orderNum, OrderStatus.NOTPAY);

                //更新支付订单映射状态
                payOrderService.updateStatusByOrderNo(orderNo, OrderStatus.REFUND_SUCCESS);

                //更新退款单
                refundInfoService.updateRefund(plainText);
            } finally {
                //要主动释放锁
                lock.unlock();
            }
        }
    }

    /**
     * wx端关单接口的调用
     *
     * @param orderNo orderNo
     * @author 林子翔
     * @since 2022/09/29
     */
    private void closeOrder(String orderNo) throws IOException {
        log.info("调用wx端关单接口,订单号：{}", orderNo);

//        创建远程请求对象
//        url为 远程微信domain 与其 关单接口 的拼接，其中%s为orderNo的占位符，需替换
        String url = wxPayConfig.getDomain()
                .concat(String.format(WxApiType.CLOSE_ORDER_BY_NO.getType(), orderNo));
        HttpPost httpPost = new HttpPost(url);

//        组装json请求参数
        Gson gson = new Gson();
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("mchid", wxPayConfig.getMchId());
        String jsonParams = gson.toJson(paramsMap);
        log.info("请求参数：{}", jsonParams);

        //        将请求参数放到请求对象中
        StringEntity entity = new StringEntity(jsonParams, "utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

//        执行请求
        CloseableHttpResponse response = wxPayClient.execute(httpPost);

        //完成签名
        try {
            int statusCode = response.getStatusLine().getStatusCode();
            //处理成功
            if (statusCode == 200) {
                log.info("success 200");
                //处理成功，无返回Body
            } else if (statusCode == 204) {
                log.info("success 204,无返回Body");
            } else {
                log.error("failed,resp code = " + statusCode);
                throw new IOException("request failed");
            }
        } finally {
            response.close();
        }
    }

    /**
     * 密文解密
     *
     * @param bodyMap wx返回的body
     * @return {@link String }
     * @author 林子翔
     * @since 2022/09/29
     */
    private String decryptFromResource(HashMap<String, Object> bodyMap) throws GeneralSecurityException {
        log.info("密文解密");

//        通知数据
        Map<String, String> resourceMap = (Map<String, String>) bodyMap.get("resource");
//        数据密文
        String ciphertext = resourceMap.get("ciphertext");
//        随机串
        String nonce = resourceMap.get("nonce");
//        附加数据
        String associatedData = resourceMap.get("associated_data");
        log.info("密文：{}", ciphertext);

        AesUtil aesUtil = new AesUtil(wxPayConfig.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        String plainText = aesUtil.decryptToString(
                associatedData.getBytes(StandardCharsets.UTF_8),
                nonce.getBytes(StandardCharsets.UTF_8),
                ciphertext);

        log.info("明文：{}", plainText);
        return plainText;
    }
}
