package com.example.projectfinder.service;

import com.example.projectfinder.model.entity.ProjectEntity;
import com.example.projectfinder.model.entity.ProjectParticipant;
import com.example.projectfinder.model.service.ProjectServiceModel;
import com.example.projectfinder.model.service.UserServiceModel;
import com.example.projectfinder.model.view.ProjectViewModel;

import java.util.List;

public interface ProjectService {

    ProjectServiceModel findProjectId(Long id);

    List<ProjectViewModel> findAllProjectViewsOrderDescId();

    List<ProjectViewModel> findProjectsByConcretTechnology();

    void createNewProject(ProjectServiceModel projectServiceModel);

    void participateInProject(Long id);

    boolean isParticipant(Long id);

    List<ProjectEntity> showCurrentUserProjects();

    List<ProjectParticipant> showAllParticipants(Long id);

    void submitLink(UserServiceModel userServiceModel, Long id);

    boolean isSubmitted(Long id);

    String findProjectTechnologyNameInString(Long id);

    List<ProjectParticipant> currentProjectUploaders(Long currentProjectId);

    List<ProjectEntity> findAllProjectsForAuthor(Long currentUserId);

    List<ProjectEntity> findAllProjectsForConcretTehnology(Long id);
}
