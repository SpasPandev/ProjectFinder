package com.example.projectfinder.service;

import com.example.projectfinder.model.entity.ProjectEntity;
import com.example.projectfinder.model.entity.ProjectParticipant;
import com.example.projectfinder.model.service.ProjectServiceModel;
import com.example.projectfinder.model.service.UserServiceModel;
import com.example.projectfinder.model.view.ProjectViewModel;

import java.util.List;

public interface ProjectService {

    ProjectServiceModel findProjectById(Long id);

    List<ProjectViewModel> findAllProjectViewsOrderDescId();

    void createNewProject(ProjectServiceModel projectServiceModel, Long currentUserId);

    void participateInProject(Long id, Long currentUserId);

    boolean isParticipant(Long id, Long currentUserId);

    List<ProjectViewModel> showCurrentUserProjects(Long currentUserId);

    List<ProjectParticipant> showAllParticipants(Long id);

    void submitLink(UserServiceModel userServiceModel, Long id, Long currentUserId);

    boolean isSubmitted(Long id, Long currentUserId);

    List<String> findProjectTechnologyNameInString(Long id);

    List<ProjectParticipant> currentProjectUploaders(Long currentProjectId);

    List<ProjectEntity> findAllProjectsForAuthor(Long currentUserId);

    List<ProjectEntity> findAllProjectsForConcreteTechnologies(List<Long> id);

    Long findProjectAuthorId(Long projectId);

    boolean isAuthor(Long projectId, Long currentUserId);

    List<ProjectViewModel> findAllDeletedProjects();
}
