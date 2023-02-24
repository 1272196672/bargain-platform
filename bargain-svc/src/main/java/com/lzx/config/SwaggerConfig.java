package com.lzx.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;


/**
 * swagger配置
 *
 * @author Bobby.zx.lin
 * @date 2023/02/24
 */
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
@Configuration
@EnableOpenApi
@ConfigurationProperties(prefix = "knife4j")
@Setter
public class SwaggerConfig {

    /**
     * yml配置中获取业务来源表map
     */
    private HashMap<String, String> swagger;

    @Bean
    Docket docket(Environment environment) {
        //正式环境不启用文档
        Profiles prod = Profiles.of("prod");
        boolean isDev = environment.acceptsProfiles(prod);
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .enable(!isDev)
                .select()
                //限定文档生成扫描包路径
                .apis(RequestHandlerSelectors.basePackage(swagger.get("base-package")))
                //需要生成文档的接口路径规则
                .paths(PathSelectors.ant("/**"))
                .build();
    }

    @Bean
    ApiInfo apiInfo() {
        Contact contact = new Contact("Bobby.zx.lin", "", "1272196672@qq.com");
        return new ApiInfo(Optional.ofNullable(swagger.get("title")).orElse(""),
                Optional.ofNullable(swagger.get("description")).orElse(""),
                Optional.ofNullable(swagger.get("version")).orElse(""),
                "",
                contact, "", "",
                new ArrayList()
        );
    }
}

