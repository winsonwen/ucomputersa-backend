package com.ucomputersa.monolithic.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 匹配所有路径
                .allowedOriginPatterns("*") // 使用 allowedOriginPatterns 允许所有域
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的 HTTP 方法
                .allowedHeaders("*") // 允许的请求头
                .allowCredentials(true);
    }
}
