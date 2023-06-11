package com.example.projectfinder.service.impl;

import com.example.projectfinder.model.entity.ProjectEntity;
import com.example.projectfinder.model.entity.ProjectParticipant;
import com.example.projectfinder.model.entity.UserEntity;
import com.example.projectfinder.model.service.ProjectServiceModel;
import com.example.projectfinder.model.service.UserServiceModel;
import com.example.projectfinder.model.view.ProjectViewModel;
import com.example.projectfinder.repository.*;
import com.example.projectfinder.service.ProjectService;
import com.example.projectfinder.service.TechnologyService;
import com.example.projectfinder.service.UserService;
import com.example.projectfinder.web.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final TechnologyService technologyService;
    private final UserRepository userRepository;
    private final ProjectParticipantRepository projectParticipantRepository;
    private final TechnologyRepository technologyRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, ModelMapper modelMapper, UserService userService, TechnologyService technologyService, UserRepository userRepository, ProjectParticipantRepository projectParticipantRepository, TechnologyRepository technologyRepository) {
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.technologyService = technologyService;
        this.userRepository = userRepository;
        this.projectParticipantRepository = projectParticipantRepository;
        this.technologyRepository = technologyRepository;
    }

    @Transactional
    @Override
    public ProjectServiceModel findProjectById(Long id) {

        ProjectEntity projectEntity = findProjectEntityById(id);

        return modelMapper.map(projectEntity, ProjectServiceModel.class);
    }

    @Transactional
    @Override
    public List<ProjectViewModel> findAllProjectViewsOrderDescId() {

        return this.projectRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream()
                .map(projectEntity -> modelMapper.map(projectEntity, ProjectViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void createNewProject(ProjectServiceModel projectServiceModel, Long currentUserId) {

        ProjectEntity projectEntity = modelMapper.map(projectServiceModel, ProjectEntity.class);

        projectEntity.setAuthor(userService.findCurrentLoginUserEntity(currentUserId));

        projectEntity.setTechnologies(projectServiceModel
                .getTechnologies()
                .stream()
                .map(technologyService::findTechnologyByName)
                .collect(Collectors.toList()));

        projectRepository.save(projectEntity);
    }

    @Override
    public void participateInProject(Long id, Long currentUserId) {

        ProjectEntity projectEntity = findProjectEntityById(id);

        UserEntity userEntity = findUserEntityById(currentUserId);

        ProjectParticipant projectParticipant = new ProjectParticipant();

        projectParticipant.setParticipant(userEntity);
        projectParticipant.setProject(projectEntity);

        projectParticipantRepository.save(projectParticipant);
    }

    @Override
    public boolean isParticipant(Long id, Long currentUserId) {

        boolean isParticipant = false;

        ProjectEntity projectEntity = findProjectEntityById(id);

        UserEntity userEntity = findUserEntityById(currentUserId);

        ProjectParticipant currentProjectWithCurrentUser = projectParticipantRepository
                .findAllByProjectAndParticipant(projectEntity, userEntity);

        if (currentProjectWithCurrentUser != null) {
            isParticipant = true;
        }

        return isParticipant;
    }

    @Transactional
    @Override
    public List<ProjectViewModel> showCurrentUserProjects(Long currentUserId) {

        List<ProjectEntity> allNotDeletedProjectsForCurrentUser =
                projectRepository.findAllUndeletedProjectsByCurrentUserId(currentUserId);

        return allNotDeletedProjectsForCurrentUser
                .stream()
                .map(project -> modelMapper.map(project, ProjectViewModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectParticipant> showAllParticipants(Long id) {

        ProjectEntity projectEntity = findProjectEntityById(id);

        return projectParticipantRepository.findAllUndeletedProjectParticipantsByProject(projectEntity);
    }

    @Override
    public boolean isSubmitted(Long id, Long currentUserId) {

        boolean isSubmitted = false;

        ProjectEntity projectEntity = findProjectEntityById(id);

        UserEntity userEntity = findUserEntityById(currentUserId);

        ProjectParticipant currentProjectWithCurrentUser = projectParticipantRepository
                .findAllByProjectAndParticipant(projectEntity, userEntity);

        if (currentProjectWithCurrentUser.getLink() != null) {
            isSubmitted = true;
        }

        return isSubmitted;
    }

    @Override
    public List<String> findProjectTechnologyNameInString(Long id) {

        List<Long> currentProjectTechnologyId = projectRepository.findTechnologyIdByProjectId(id);

        return technologyRepository.findTechnologyNameInStringById(currentProjectTechnologyId);
    }

    @Override
    public void submitLink(UserServiceModel userServiceModel, Long id, Long currentUserId) {

        ProjectEntity currentProject = findProjectEntityById(id);

        UserEntity currentUserEntity = findUserEntityById(currentUserId);

        ProjectParticipant projectParticipant = projectParticipantRepository
                .findAllByProjectAndParticipant(currentProject, currentUserEntity);

        projectParticipant.setLink(userServiceModel.getLink());

        projectParticipantRepository.save(projectParticipant);
    }

    @Override
    public List<ProjectParticipant> currentProjectUploaders(Long currentProjectId) {

        return projectParticipantRepository
                .findAllByProjectIdAndLinkIsNotNullAndParticipantIsUndeleted(currentProjectId);
    }

    @Override
    public List<ProjectEntity> findAllProjectsForAuthor(Long currentUserId) {

        return projectRepository.findAllByAuthor_Id(currentUserId);
    }

    @Override
    public List<ProjectEntity> findAllProjectsForConcreteTechnologies(List<Long> id) {

        return projectRepository.findAllUndeletedProjectsByTechnologiesIn(id);
    }

    @Override
    public Long findProjectAuthorId(Long projectId) {

        return findProjectEntityById(projectId).getAuthor().getId();
    }

    @Override
    public boolean isAuthor(Long projectId, Long currentUserId) {

        return currentUserId.equals(findProjectAuthorId(projectId));
    }

    @Transactional
    @Override
    public List<ProjectViewModel> findAllDeletedProjects() {

        return projectRepository.findAllDeletedProjects()
                .stream()
                .map(projectEntity -> modelMapper.map(projectEntity, ProjectViewModel.class))
                .collect(Collectors.toList());
    }

    private UserEntity findUserEntityById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("User with id: " + id + " was not found!"));
    }

    private ProjectEntity findProjectEntityById(Long id) {
        return projectRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("ProjectEntity with id: " + id + " was not found!"));
    }
}
