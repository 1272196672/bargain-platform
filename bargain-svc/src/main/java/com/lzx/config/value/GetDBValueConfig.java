package com.lzx.config.value;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GetDBValueConfig {
    public static String DATASOURCE_DRUID_URL; // url

    public static String DATASOURCE_DRUID_USERNAME; // 用户名

    public static String DATASOURCE_DRUID_PASSWORD; // 密码

    @Value("${spring.datasource.druid.url}")
    public void getUrl(String url) {
        DATASOURCE_DRUID_URL = url;
    }

    @Value("${spring.datasource.druid.username}")
    public void getUsername(String username) {
        DATASOURCE_DRUID_USERNAME = username;
    }

    @Value("${spring.datasource.druid.password}")
    public void getPassword(String password) {
        DATASOURCE_DRUID_PASSWORD = password;
    }
}
