package com.yqsas.demo.common.config;

import com.yqsas.demo.common.web.interceptor.LocaleInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 * @author yqsas
 */
@Configuration
public class WebBaseConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleInterceptor()).addPathPatterns("/**")
            .excludePathPatterns("*.js", "/**/*.js", "*.css", "/**/*.css", "*.html", "/**/*.html");
    }
}
