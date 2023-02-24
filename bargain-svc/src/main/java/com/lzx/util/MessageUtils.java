
package com.lzx.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;

/**
 * 错误提示国际化
 *
 * @author Anonymous
 * @since 1.0.0
 */
@Slf4j
public class MessageUtils {
    private static MessageSource messageSource;

    static {
        messageSource = (MessageSource) SpringContextUtils.getBean("messageSource");
    }

    public static String getMessage(int code) {
        return getMessage(code, new String[0]);
    }

    public static String getMessage(int code, String... params) {
        return messageSource.getMessage(code + "", params, HttpContextUtils.getLocale());
    }

}
