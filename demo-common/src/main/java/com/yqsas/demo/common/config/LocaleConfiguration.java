package com.yqsas.demo.common.config;

import com.yqsas.demo.common.i18n.LocaleHeaderLocaleResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * 语言区域配置
 * @author yqsas
 */
@Configuration
public class LocaleConfiguration {

    private final String LOCAL_HEAD_NAME = "Locale";

    @Bean
    public LocaleResolver localeResolver() {
        LocaleHeaderLocaleResolver localeResolver = new LocaleHeaderLocaleResolver();
        localeResolver.setLocaleHeadName(LOCAL_HEAD_NAME);
        localeResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return localeResolver;
    }

    @Bean
    public WebMvcConfigurer localeInterceptor() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
                localeInterceptor.setParamName(LOCAL_HEAD_NAME);
                registry.addInterceptor(localeInterceptor);
            }
        };
    }
}
