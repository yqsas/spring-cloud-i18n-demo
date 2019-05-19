package com.yqsas.demo.common.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 国际化转换工具
 * @author yqsas
 */
@Component
public class LocaleUtils {

    private static final ThreadLocal<Locale> localeThreadLocal = new ThreadLocal<>();

    private static MessageSource messageSource;
    public LocaleUtils(MessageSource messageSource) {
        LocaleUtils.messageSource = messageSource;
    }

    public static String getMessage(String msgKey) {
        return getMessage(msgKey, null);
    }

    public static String getMessage(String msgKey,Object[] args) {
        try {
            return messageSource.getMessage(msgKey, args, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return msgKey;
        }
    }

    public static Locale getCurrentLocale() {
        Locale locale = localeThreadLocal.get();
        if(locale == null){
            //默认简体中文
            locale = Locale.CHINA;
        }
        return locale;
    }

    public static void setCurrentLocale(Locale locale) {
        localeThreadLocal.set(locale);
    }


}
