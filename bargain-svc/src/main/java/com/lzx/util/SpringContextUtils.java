package com.lzx.util;

import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Spring 上下文 工具类
 *
 * @author Anonymous
 */
@Component
@Lazy(false)
public class SpringContextUtils implements ApplicationContextAware {
    public static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    public static <T> T getBean(String name, Class<T> requiredType) {
        return applicationContext.getBean(name, requiredType);
    }
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public static boolean isSingleton(String name) {
        return applicationContext.isSingleton(name);
    }

    public static Class<? extends Object> getType(String name) {
        return applicationContext.getType(name);
    }

    @SneakyThrows
    public static Method getMethod(HttpServletRequest request) {
        RequestMappingHandlerMapping handlerMapping = getBean(RequestMappingHandlerMapping.class);
        //RequestMappingHandlerMapping是对应url和处理类方法的一个类
        HandlerExecutionChain handlerChain = handlerMapping.getHandler(request);
        //通过处理链找到对应的HandlerMethod类
        return Optional.ofNullable(handlerChain)
                .map(m -> (HandlerMethod) m.getHandler())
                .map(HandlerMethod::getMethod)
                .orElse(null);
    }
}