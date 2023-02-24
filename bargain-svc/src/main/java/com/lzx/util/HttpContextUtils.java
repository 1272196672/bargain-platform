package com.lzx.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

/**
 * 获取request请求相关参数
 *
 * @author Anonymous
 */
@Slf4j
public class HttpContextUtils {

    /**
     * 获取HttpServletRequest对象
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    }


    /**
     * 获取post请求参数
     *
     * @param request
     * @return
     */
    public static String getPostData(HttpServletRequest request) {
        StringBuffer data = new StringBuffer();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while (Objects.nonNull(line = reader.readLine())) {
                data.append(line);
            }
        } catch (IOException e) {
            log.error("获取post参数失败", e);
        }
        return data.toString();
    }

    /**
     * 获取get请求参数
     *
     * @param request
     * @return
     */
    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        Enumeration<String> parameters = request.getParameterNames();
        Map<String, String> params = new HashMap(16);
        while (parameters.hasMoreElements()) {
            String parameter = parameters.nextElement();
            String value = request.getParameter(parameter);
            if (StringUtils.isNotBlank(value)) {
                params.put(parameter, value);
            }
        }
        return params;
    }

    public static String getDomain() {
        HttpServletRequest request = getRequest();
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
    }

    public static String getOrigin() {
        HttpServletRequest request = getRequest();
        return request.getHeader(HttpHeaders.ORIGIN);
    }

    /**
     * 从请求头中获取参数
     *
     * @return
     */
    public static String getHeaderParam(String paramName) {
        //request
        HttpServletRequest request = getRequest();
        //从header中获取
        String param = request.getHeader(paramName);
        //如果header中不存在，则从参数中获取
        if (StringUtils.isNotBlank(param)) {
            return param;
        }
        return request.getParameter(paramName);
    }

    /**
     * 获取语言类型
     *
     * @return
     */
    public static Locale getLocale() {
        String language = getHeaderParam(HttpHeaders.ACCEPT_LANGUAGE);
        if ("en".equalsIgnoreCase(language)) {
            return new Locale("en", "US");
        } else if ("zh-TW".equalsIgnoreCase(language)) {
            return new Locale("zh", "TW");
        } else {
            return new Locale("zh", "CN");
        }
    }

}