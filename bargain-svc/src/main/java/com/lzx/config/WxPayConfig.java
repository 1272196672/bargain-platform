package com.lzx.config;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;

/**
 * @author 林子翔
 * @since 2022 09 2022/9/28
 */
@Configuration
@PropertySource("classpath:wxPay/wxpay.properties")
@ConfigurationProperties(prefix = "wxpay")
@Data
@Slf4j
public class WxPayConfig {
    private String mchId;
    private String mchSerialNo;
    private String privateKeyPath;
    private String apiV3Key;
    private String appId;
    private String appSecret;
    private String domain;
    private String notifyDomain;

    public String getPrivateKeyPath() {
        try {
            return new String(privateKeyPath.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("错误！获取商户密钥时转String出错，请检查！【" + e.getMessage() + "】");
            return null;
        }
    }

    /**
     * 获取商户私钥文件
     *
     * @param path 路径
     * @return {@link PrivateKey }
     * @author 林子翔
     * @since 2022/09/28
     */
    private PrivateKey getPrivateKey(String path) {
        try {
            return PemUtil.loadPrivateKey(
                    new FileInputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取签名验证器
     *
     * @return {@link Verifier }
     * @author 林子翔
     * @since 2022/09/28
     */
    @Bean
    public Verifier getVerifier() throws GeneralSecurityException, IOException, HttpCodeException, NotFoundException {
        log.info("获取签名验证器");
        //        获取商户私钥
        log.info("商户私钥路径【" + getPrivateKeyPath() + "】");
        PrivateKey privateKey = getPrivateKey(getPrivateKeyPath());
        log.info("获取到的商户私钥【" + privateKey + "】");
        // 获取证书管理器实例
        CertificatesManager certificatesManager = CertificatesManager.getInstance();
        // 向证书管理器增加需要自动更新平台证书的商户信息
        certificatesManager.putMerchant(mchId, new WechatPay2Credentials(mchId,
                new PrivateKeySigner(mchSerialNo, privateKey)), apiV3Key.getBytes(StandardCharsets.UTF_8));
        // ... 若有多个商户号，可继续调用putMerchant添加商户信息

        // 从证书管理器中获取verifier
        return certificatesManager.getVerifier(mchId);
    }

    /**
     * 获取WxPayClient（HttpClient）
     *
     * @param verifier 验证器
     * @return {@link CloseableHttpClient }
     * @author 林子翔
     * @since 2022/09/28
     */
    @Bean
    public CloseableHttpClient getWxPayClient(Verifier verifier) {
        log.info("获取WxPayClient（HttpClient）");
        //        获取商户私钥
        PrivateKey privateKey = getPrivateKey(getPrivateKeyPath());
        log.info("获取到的商户私钥【" + privateKey + "】");
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(mchId, mchSerialNo, privateKey)
                .withValidator(new WechatPay2Validator(verifier));
        // ... 接下来，你仍然可以通过builder设置各种参数，来配置你的HttpClient

        // 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签，并进行证书自动更新
        return builder.build();
    }
}
