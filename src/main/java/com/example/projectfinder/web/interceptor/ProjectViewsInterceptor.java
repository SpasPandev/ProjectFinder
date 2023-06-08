package com.example.projectfinder.web.interceptor;

import com.example.projectfinder.service.ProjectViewsInterceptorService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ProjectViewsInterceptor implements HandlerInterceptor {

    private final ProjectViewsInterceptorService projectViewsInterceptorService;

    public ProjectViewsInterceptor(ProjectViewsInterceptorService projectViewsInterceptorService) {
        this.projectViewsInterceptorService = projectViewsInterceptorService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String requestPath = request.getRequestURI();

        if (isProjectPage(requestPath)) {
            projectViewsInterceptorService.increaseViewsCount(request.getRequestURI());
        }

        return true;
    }

    private boolean isProjectPage(String requestPath) {

        String projectPagePattern = "/project/\\d+";

        return requestPath.matches(projectPagePattern);
    }
}
