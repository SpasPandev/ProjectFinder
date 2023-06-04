package com.example.projectfinder.service.impl;

import com.example.projectfinder.model.entity.ProjectEntity;
import com.example.projectfinder.model.entity.ProjectParticipant;
import com.example.projectfinder.model.entity.UserEntity;
import com.example.projectfinder.model.entity.enums.TechnologyNameEnum;
import com.example.projectfinder.model.service.ProjectServiceModel;
import com.example.projectfinder.model.service.UserServiceModel;
import com.example.projectfinder.model.view.ProjectViewModel;
import com.example.projectfinder.repository.*;
import com.example.projectfinder.service.ProjectService;
import com.example.projectfinder.service.TechnologyService;
import com.example.projectfinder.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

    @Override
    public ProjectServiceModel findProjectById(Long id) {
        ProjectServiceModel projectServiceModel = projectRepository
                .findById(id)
                .map(projectEntity -> modelMapper.map(projectEntity, ProjectServiceModel.class))
                .orElse(null);
        return projectServiceModel;

    }

    @Override
    public List<ProjectViewModel> findAllProjectViewsOrderDescId() {

        List<ProjectViewModel> allProjectViewsList =
         this.projectRepository.findAllByDeletedIsFalseOrderByIdDesc()
                .stream().map(projectEntity -> {
                    ProjectViewModel projectViewModel = modelMapper.map(projectEntity, ProjectViewModel.class);

                    return projectViewModel;
                })
                .collect(Collectors.toList());

        return allProjectViewsList;
    }

    @Override
    public void createNewProject(ProjectServiceModel projectServiceModel, Long currentUserId) {

        ProjectEntity projectEntity = modelMapper.map(projectServiceModel, ProjectEntity.class);

        projectEntity.setAuthor(userService.findCurrentLoginUserEntity(currentUserId));

        projectEntity.setTechnologies(projectServiceModel
                .getTechnologies()
                .stream()
                .map((TechnologyNameEnum technologyNameEnum) -> technologyService.findTechnologyByName(technologyNameEnum))
                .collect(Collectors.toSet()));

        projectRepository.save(projectEntity);
    }

    @Override
    public void participateInProject(Long id, Long currentUserId) {

        ProjectEntity projectEntity = projectRepository.findById(id).get();

        UserEntity userEntity = userRepository.findById(currentUserId).get();

        ProjectParticipant projectParticipant = new ProjectParticipant();

        projectParticipant.setParticipant(userEntity);
        projectParticipant.setProject(projectEntity);

        projectParticipantRepository.save(projectParticipant);
    }

    @Override
    public boolean isParticipant(Long id, Long currentUserId) {

        boolean isParticipant = false;

        ProjectEntity projectEntity = projectRepository.findById(id).get();

        UserEntity userEntity = userRepository.findById(currentUserId).get();

        ProjectParticipant currentProjectWithCurrentUser = projectParticipantRepository
                .findCurrentUserAndCurrentProject(projectEntity, userEntity);

        if (currentProjectWithCurrentUser != null)
        {
            isParticipant = true;
        }

        return isParticipant;
    }

    @Override
    public List<ProjectViewModel> showCurrentUserProjects(Long currentUserId)
    {
        List<Long> listOfAllProjectsIds = projectRepository.listOfAllProjectsIds(currentUserId);

        List<ProjectViewModel> listOfCurrentUserProjects = new ArrayList<>();

        for (int i = listOfAllProjectsIds.size() - 1; i >= 0; i--) {

            if (!projectRepository.findById(listOfAllProjectsIds.get(i)).get().isDeleted())
            {
                listOfCurrentUserProjects
                        .add(modelMapper.map(projectRepository.findById(listOfAllProjectsIds.get(i)).get(), ProjectViewModel.class));
            }
        }

        return listOfCurrentUserProjects;
    }

    @Override
    public List<ProjectParticipant> showAllParticipants(Long id)
    {
        List<ProjectParticipant> allProjectParticipants = projectParticipantRepository
                .findProjectParticipantByProject(projectRepository.findById(id).get());

        for (int i = 0; i < allProjectParticipants.size(); i++) {

            if (allProjectParticipants.get(i).getParticipant().isDeleted())
            {
                allProjectParticipants.remove(i);
                i--;
            }
        }

        return allProjectParticipants;
    }

    @Override
    public boolean isSubmitted(Long id, Long currentUserId) {

        boolean isSubmitted = false;

        ProjectEntity projectEntity = projectRepository.findById(id).get();

        UserEntity userEntity = userRepository.findById(currentUserId).get();

        ProjectParticipant currentProjectWithCurrentUser = projectParticipantRepository
                .findCurrentUserAndCurrentProject(projectEntity, userEntity);

        if (currentProjectWithCurrentUser.getLink() != null)
        {
            isSubmitted = true;
        }

        return isSubmitted;
    }

    @Override
    public List<String> findProjectTechnologyNameInString(Long id) {

        List<Long> currentProjectTechnologyId = projectRepository.findTechnologyIdByProjectId(id);

        List<String> currentProjectTechnologyName = technologyRepository.findTechnologyNameInStringById(currentProjectTechnologyId);

        return currentProjectTechnologyName;
    }

    @Override
    public void submitLink(UserServiceModel userServiceModel, Long id, Long currentUserId) {

        ProjectEntity currentProject = projectRepository.findById(id).get();

        UserEntity currentUserEntity = userRepository.findById(currentUserId).get();

        ProjectParticipant projectParticipant = projectParticipantRepository
                .findCurrentUserAndCurrentProject(currentProject, currentUserEntity);

        projectParticipant.setLink(userServiceModel.getLink());

        projectParticipantRepository.save(projectParticipant);
    }

    @Override
    public List<ProjectParticipant> currentProjectUploaders(Long currentProjectId)
    {
        List<ProjectParticipant> listOfAllProjectParticipantsUploadedOnCurrentProject =
                projectParticipantRepository.findAllProjectParticipantsUploadedOnCurrentProject(currentProjectId);

        for (int i = 0; i < listOfAllProjectParticipantsUploadedOnCurrentProject.size(); i++) {

            if (listOfAllProjectParticipantsUploadedOnCurrentProject.get(i).getParticipant().isDeleted())
            {
                listOfAllProjectParticipantsUploadedOnCurrentProject.remove(i);
                i--;
            }
        }

        return listOfAllProjectParticipantsUploadedOnCurrentProject;
    }

    @Override
    public List<ProjectEntity> findAllProjectsForAuthor(Long currentUserId)
    {
        List<ProjectEntity> allProjectsForAuthor =  projectRepository.findAllProjectsForAuthor(currentUserId);

        return allProjectsForAuthor;
    }

    @Override
    public List<ProjectEntity> findAllProjectsForConcretTehnology(List<Long> id)
    {
        Set<Long> listOfProjectIds = projectRepository.findListOfProjectsIdsForConcretTehnologies(id);

        List<ProjectEntity> listOfAllProjectsForConcretTehnology = new ArrayList<>();

        for (Long element : listOfProjectIds) {

            ProjectEntity project = projectRepository.findById(element).get();

            if (project.isDeleted() == false) {
                listOfAllProjectsForConcretTehnology.add(project);
            }
        }

        return listOfAllProjectsForConcretTehnology;
    }

    @Override
    public Long findProjectAuthorId(Long projectId) {

        return projectRepository.findById(projectId).get().getAuthor().getId();
    }

    @Override
    public boolean isAuthor(Long projectId, Long currentUserId) {

        return currentUserId.equals(findProjectAuthorId(projectId));
    }

    @Override
    public List<ProjectViewModel> findAllDeletedProjects() {
        return projectRepository.findAllDeletedProjects()
                .stream()
                .map(projectEntity -> {
                    ProjectViewModel projectViewModel = modelMapper.map(projectEntity, ProjectViewModel.class);
                    return projectViewModel;
                })
                .collect(Collectors.toList());
    }

}
