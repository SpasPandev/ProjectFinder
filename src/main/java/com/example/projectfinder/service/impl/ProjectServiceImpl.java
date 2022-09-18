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
import com.example.projectfinder.util.CurrentUser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final TechnologyService technologyService;
    private final UserRepository userRepository;
    private final CurrentUser currentUser;
    private final ProjectParticipantRepository projectParticipantRepository;
    private final TechnologyRepository technologyRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, ModelMapper modelMapper, UserService userService, TechnologyService technologyService, UserRepository userRepository, CurrentUser currentUser, ProjectParticipantRepository projectParticipantRepository, TechnologyRepository technologyRepository) {
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.technologyService = technologyService;
        this.userRepository = userRepository;
        this.currentUser = currentUser;
        this.projectParticipantRepository = projectParticipantRepository;
        this.technologyRepository = technologyRepository;
    }

    @Override
    public ProjectServiceModel findProjectId(Long id) {
        return projectRepository
                .findById(id)
                .map(projectEntity -> modelMapper.map(projectEntity, ProjectServiceModel.class))
                .orElse(null);

    }

    @Override
    public List<ProjectViewModel> findAllProjectViewsOrderDescId() {

        List<ProjectViewModel> allProjectViewsList =
         this.projectRepository.findAllByOrderByIdDesc()
                .stream().map(projectEntity -> {
                    ProjectViewModel projectViewModel = modelMapper.map(projectEntity, ProjectViewModel.class);

                    return projectViewModel;
                })
                .collect(Collectors.toList());

        return allProjectViewsList;
    }

    @Override
    public List<ProjectViewModel> findProjectsByConcretTechnology() {

        List<ProjectViewModel> allProjectViewsList =
                this.projectRepository.findAll()
                        .stream().map(projectEntity -> {
                            ProjectViewModel projectViewModel = modelMapper.map(projectEntity, ProjectViewModel.class);

                            return projectViewModel;
                        })
                        .collect(Collectors.toList());

        List<ProjectViewModel> projectViewsListWithConcretTechnology = allProjectViewsList;

        int k = -1;

        for (int i = 0; i < allProjectViewsList.size(); i++)
        {
            k++;
            if (!allProjectViewsList.get(i).getTechnologies().equals(TechnologyNameEnum.PYTHON))
            {
                projectViewsListWithConcretTechnology
                        .remove(allProjectViewsList.get(k));
                k--;
                System.out.println("k = " + k);
            }
        }

        return projectViewsListWithConcretTechnology;
    }

    @Override
    public void createNewProject(ProjectServiceModel projectServiceModel) {

        ProjectEntity projectEntity = modelMapper.map(projectServiceModel, ProjectEntity.class);

        projectEntity.setAuthor(userService.findCurrentLoginUserEntity());

        projectEntity.setTechnologies(projectServiceModel
                .getTechnologies()
                .stream()
                .map(technologyService::findTechnologyByName)
                .collect(Collectors.toSet()));

        projectRepository.save(projectEntity);
    }

    @Override
    public void participateInProject(Long id) {

        ProjectEntity projectEntity = projectRepository.findById(id).get();

        UserEntity userEntity = userRepository.findById(currentUser.getId()).get();

        ProjectParticipant projectParticipant = new ProjectParticipant();

        projectParticipant.setParticipant(userEntity);
        projectParticipant.setProject(projectEntity);

        userEntity.addProjectForParticipant(projectParticipant);

        projectParticipantRepository.save(projectParticipant);
    }

    @Override
    public boolean isParticipant(Long id) {

        boolean isParticipant = true;

        ProjectEntity projectEntity = projectRepository.findById(id).get();
        UserEntity userEntity = userRepository.findById(currentUser.getId()).get();

        ProjectParticipant currentProjectWithCurrentUser = projectParticipantRepository
                .findCurrentUserAndCurrentProject(projectEntity, userEntity);

        if (currentProjectWithCurrentUser != null)
        {
            isParticipant = false;
        }

        return isParticipant;
    }

    @Override
    public List<ProjectEntity> showCurrentUserProjects()
    {
        List<Long> listOfAllProjectsIds = projectRepository.listOfAllProjectsIds(currentUser.getId());

        List<ProjectEntity> listOfCurrentUserProjects = new ArrayList<>();

        for (int i = listOfAllProjectsIds.size() - 1; i >= 0; i--) {

            listOfCurrentUserProjects
                    .add(projectRepository.findById(listOfAllProjectsIds.get(i)).get());
        }

        return listOfCurrentUserProjects;
    }

    @Override
    public List<ProjectParticipant> showAllParticipants(Long id)
    {
        List<ProjectParticipant> allProjectParticipants = projectParticipantRepository
                .findProjectParticipantByProject(projectRepository.findById(id).get());

        return allProjectParticipants;
    }

    @Override
    public boolean isSubmitted(Long id) {

        boolean isSubmitted = true;

        ProjectEntity projectEntity = projectRepository.findById(id).get();
        UserEntity userEntity = userRepository.findById(currentUser.getId()).get();

        ProjectParticipant currentProjectWithCurrentUser = projectParticipantRepository
                .findCurrentUserAndCurrentProject(projectEntity, userEntity);

        if (currentProjectWithCurrentUser.getLink() != null)
        {
            isSubmitted = false;
        }

        return isSubmitted;
    }

    @Override
    public String findProjectTechnologyNameInString(Long id) {

        Long currentProjectTechnologyId = projectRepository.findTechnologyIdByProjectId(id);

        String currentProjectTechnologyName = technologyRepository.findTechnologyNameInStringById(currentProjectTechnologyId);

        return currentProjectTechnologyName;
    }

    @Override
    public void submitLink(UserServiceModel userServiceModel, Long id) {


        ProjectEntity currentProject = projectRepository.findById(id).get();
        UserEntity currentUserEntity = userRepository.findById(currentUser.getId()).get();

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

        return listOfAllProjectParticipantsUploadedOnCurrentProject;
    }

    @Override
    public List<ProjectEntity> findAllProjectsForAuthor(Long currentUserId)
    {
        List<ProjectEntity> allProjectsForAuthor =  projectRepository.findAllProjectsForAuthor(currentUserId);

        return allProjectsForAuthor;
    }

    @Override
    public List<ProjectEntity> findAllProjectsForConcretTehnology(Long id)
    {
        List<Long> listOfProjectIds = projectRepository.findListOfProjectsIdsForConcretTehnology(id);

        List<ProjectEntity> listOfAllProjectsForConcretTehnology = new ArrayList<>();

        for (int i = 0; i < listOfProjectIds.size(); i++) {

            listOfAllProjectsForConcretTehnology
                    .add(projectRepository.findById(listOfProjectIds.get(i)).get());
        }

        return listOfAllProjectsForConcretTehnology;
    }

}
