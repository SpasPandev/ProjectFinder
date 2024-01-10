package com.example.projectfinder.service;

import com.example.projectfinder.model.entity.ProjectEntity;
import com.example.projectfinder.repository.ProjectRepository;
import com.example.projectfinder.web.exception.ObjectNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectViewsInterceptorService {

    private final ProjectRepository projectRepository;

    public ProjectViewsInterceptorService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    public void increaseViewsCount(String requestPath) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long currentUserId = getCurrentUserId(authentication);
        Long projectId = Long.valueOf(requestPath.substring(requestPath.lastIndexOf("/") + 1));

        if (projectRepository.isCurrentUserAuthorForThisProject(currentUserId, projectId)) {
            return;
        }

        ProjectEntity projectEntity = projectRepository.findById(projectId).orElseThrow(() ->
                new ObjectNotFoundException("Project with id: " + projectId + " was not found"));

        projectEntity.setViewsCount(projectEntity.getViewsCount() + 1);
        projectRepository.save(projectEntity);
    }

    private Long getCurrentUserId(Authentication authentication) {

        ApplicationUser currentUser = (ApplicationUser) authentication.getPrincipal();

        return currentUser.getId();
    }
}
