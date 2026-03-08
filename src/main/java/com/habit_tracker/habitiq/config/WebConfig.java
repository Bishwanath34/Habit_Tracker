package com.habit_tracker.habitiq.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(authInterceptor)

                .addPathPatterns("/api/**")

                .excludePathPatterns(
                        "/api/v1/auth/**",
                        "/css/**",
                        "/js/**",
                        "/images/**"
                );
    }
}