package com.yqsas.demo.common.web.interceptor;

import com.yqsas.demo.common.util.LocaleUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class LocaleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Locale locale = RequestContextUtils.getLocaleResolver(request).resolveLocale(request);
        LocaleUtils.setCurrentLocale(locale);
        return true;
    }
}
