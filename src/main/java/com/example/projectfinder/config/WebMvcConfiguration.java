package com.example.projectfinder.config;

import com.example.projectfinder.web.interceptor.ProjectViewsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    private final ProjectViewsInterceptor projectViewsInterceptor;

    public WebMvcConfiguration(ProjectViewsInterceptor projectViewsInterceptor) {
        this.projectViewsInterceptor = projectViewsInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(projectViewsInterceptor);
    }
}
