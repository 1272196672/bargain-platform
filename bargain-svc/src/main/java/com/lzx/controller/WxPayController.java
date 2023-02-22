package com.lzx.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.google.gson.Gson;
import com.lzx.service.WxPayService;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * wx支付控制器
 *
 * @author 林子翔
 * @date 2022/10/11
 * @since 2022 09 2022/9/28
 */
@CrossOrigin
@RestController
@RequestMapping("/api/wx-pay")
@Api(tags = "网站微信支付api")
@Slf4j
public class WxPayController {
    @Resource
    private WxPayService wxPayService;

    @Autowired
    private Verifier verifier;

    /**
     * 调用native统一下单api，生产支付二维码
     *
     * @param orderOriginNum 原订单编号
     * @return {@link R }
     * @author 林子翔
     * @since 2022/09/29
     */
    @Cross
    @PostMapping("/native/{orderOriginNum}")
    @ApiOperation("调用native统一下单api，生产支付二维码")
    public Result nativePay(@PathVariable String orderOriginNum) throws Exception {
        log.info("发起支付请求");
//        防止用户操作失误导致意外发生，在支付时首先将此订单关联的所有未支付订单给取消
        this.cancel(orderOriginNum);
//        返回二维码与订单号
        return Result.success(wxPayService.nativePay(orderOriginNum));
//        return R.ok().setData(wxPayService.nativePay(productId));
    }

    /**
     * 调用h5统一下单api，生产支付二维码
     *
     * @param orderOriginNum 原订单编号
     * @param ip             手机ip地址
     * @param phoneTye       手机类型
     * @return {@link Result }
     * @author 林子翔
     * @since 2022/10/08
     */
    @Cross
    @PostMapping("/h5/{orderOriginNum}/{ip}/{phoneTye}")
    @ApiOperation("调用h5统一下单api，生产支付二维码")
    public Result h5Pay(@PathVariable String orderOriginNum, @PathVariable String ip, @PathVariable String phoneTye) throws Exception {
        log.info("发起支付请求");
//        防止用户操作失误导致意外发生，在支付时首先将此订单关联的所有未支付订单给取消
        this.cancel(orderOriginNum);
//        返回二维码与订单号
        return Result.success(wxPayService.h5Pay(orderOriginNum, ip, phoneTye));
    }

    /**
     * wx返回通知，我们处理
     *
     * @param request  request
     * @param response response
     * @return {@link String }
     * @author 林子翔
     * @since 2022/09/29
     */
    @ApiOperation("native支付结果通知")
    @PostMapping("/native/notify")
    @Cross
    public String nativeNotify(HttpServletRequest request, HttpServletResponse response) {
//        创建应答对象
        HashMap<String, String> respMap = new HashMap<>();
        try {
//        处理通知参数
            String body = HttpUtils.readData(request);
            HashMap<String, Object> bodyMap = new Gson().fromJson(body, HashMap.class);
            String requestId = (String) bodyMap.get("id");
            log.info("支付通知的id：{}", requestId);
            log.info("支付通知的完整数据：{}", body);
//        验签
            WechatPay2Validator4Request wechatPay2Validator4Request = new WechatPay2Validator4Request(verifier, requestId, body);
            if (!wechatPay2Validator4Request.validate(request)) {
                log.error("通知验签失败");
                //        失败应答
                response.setStatus(500);
                respMap.put("code", "FAIL");
                respMap.put("message", "通知验签失败");
            }
            log.info("通知验签成功");

//        处理订单
            wxPayService.processOrder(bodyMap);

//        成功应答
            response.setStatus(200);
            respMap.put("code", "SUCCESS");
            respMap.put("message", "成功应答");
        } catch (Exception e) {
//        失败应答
            response.setStatus(500);
            respMap.put("code", "FAIL");
            respMap.put("message", "失败应答");
        }
        return new Gson().toJson(respMap);
    }

    /**
     * h5支付结果通知
     *
     * @param request  request
     * @param response response
     * @return {@link String }
     * @author 林子翔
     * @since 2022/09/30
     */
    @Cross
    @ApiOperation("h5支付结果通知")
    @PostMapping("/h5/notify")
    public String h5Notify(HttpServletRequest request, HttpServletResponse response) {
//        创建应答对象
        HashMap<String, String> respMap = new HashMap<>();
        try {
//        处理通知参数
            String body = HttpUtils.readData(request);
            HashMap<String, Object> bodyMap = new Gson().fromJson(body, HashMap.class);
            String requestId = (String) bodyMap.get("id");
            log.info("支付通知的id：{}", requestId);
            log.info("支付通知的完整数据：{}", body);
//        验签
            WechatPay2Validator4Request wechatPay2Validator4Request = new WechatPay2Validator4Request(verifier, requestId, body);
            if (!wechatPay2Validator4Request.validate(request)) {
                log.error("通知验签失败");
                //        失败应答
                response.setStatus(500);
                respMap.put("code", "FAIL");
                respMap.put("message", "通知验签失败");
            }
            log.info("通知验签成功");

//        处理订单
            wxPayService.processOrder(bodyMap);

//        成功应答
            response.setStatus(200);
            respMap.put("code", "SUCCESS");
            respMap.put("message", "成功应答");
        } catch (Exception e) {
//        失败应答
            response.setStatus(500);
            respMap.put("code", "FAIL");
            respMap.put("message", "失败应答");
        }
        return new Gson().toJson(respMap);
    }

    @PostMapping("/cancel/{orderOriginNum}")
    @ApiOperation("取消订单")
    @Cross
    public Result cancel(@PathVariable String orderOriginNum) throws Exception {
        log.info("取消订单");

        wxPayService.cancelOrder(orderOriginNum);
        return Result.success("订单已取消", orderOriginNum);
//        return R.ok().setMsg("订单已取消");
    }

    @GetMapping("/query/{orderOriginNum}")
    @ApiOperation("向wx端查询订单")
    @Cross
    public Result queryOrder(@PathVariable String orderOriginNum) throws Exception {
        log.info("向wx端查询订单");

        HashMap<String, String> result = wxPayService.queryOrder(orderOriginNum);
        return Result.success("查询成功", result);
//        return R.ok().setMsg("查询成功").data("result", result);
    }

    /**
     * 申请退款
     *
     * @param orderOriginNum 订单号
     * @param reason  原因
     * @return {@link R }
     * @author 林子翔
     * @since 2022/09/30
     */
    @PostMapping("/refunds/{orderOriginNum}/{reason}")
    @ApiOperation("申请退款")
    @Cross
    public Result refunds(@PathVariable String orderOriginNum, @PathVariable String reason) throws Exception {
        log.info("申请退款");
        wxPayService.refund(orderOriginNum, reason);
        return Result.success(orderOriginNum);
//        return R.ok();
    }

    @ApiOperation("向wx端查询退款")
    @GetMapping("/query-refund/{refundNo}")
    @Cross
    public Result queryRefund(@PathVariable String refundNo) throws Exception {

        log.info("查询退款");

        String result = wxPayService.queryRefund(refundNo);
        return Result.success("查询成功", result);
//        return R.ok().setMsg("查询成功").data("result", result);
    }

    @ApiOperation("退款结果通知")
    @PostMapping("/refunds/notify")
    @Cross
    public String refundsNotify(HttpServletRequest request, HttpServletResponse response) {

        log.info("退款通知执行");
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<>();

        try {
            //处理通知参数
            String body = HttpUtils.readData(request);
            HashMap<String, Object> bodyMap = gson.fromJson(body, HashMap.class);
            String requestId = (String) bodyMap.get("id");
            log.info("支付通知的id ===> {}", requestId);

            //签名的验证
            WechatPay2Validator4Request wechatPay2ValidatorForRequest
                    = new WechatPay2Validator4Request(verifier, requestId, body);
            if (!wechatPay2ValidatorForRequest.validate(request)) {

                log.error("通知验签失败");
                //失败应答
                response.setStatus(500);
                map.put("code", "Fail");
                map.put("message", "通知验签失败");
                return gson.toJson(map);
            }
            log.info("通知验签成功");

            //处理退款单
            wxPayService.processRefund(bodyMap);

            //成功应答
            response.setStatus(200);
            map.put("code", "SUCCESS");
            map.put("message", "成功");
            return gson.toJson(map);

        } catch (Exception e) {
            e.printStackTrace();
            //失败应答
            response.setStatus(500);
            map.put("code", "ERROR");
            map.put("message", "失败");
            return gson.toJson(map);
        }
    }
}
