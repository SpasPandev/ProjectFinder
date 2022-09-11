package com.example.projectfinder.service.impl;

import com.example.projectfinder.model.entity.ProjectEntity;
import com.example.projectfinder.model.entity.UserEntity;
import com.example.projectfinder.model.entity.enums.TechnologyNameEnum;
import com.example.projectfinder.model.service.ProjectServiceModel;
import com.example.projectfinder.model.view.ProjectViewModel;
import com.example.projectfinder.repository.ProjectRepository;
import com.example.projectfinder.repository.UserRepository;
import com.example.projectfinder.service.ProjectService;
import com.example.projectfinder.service.TechnologyService;
import com.example.projectfinder.service.UserService;
import com.example.projectfinder.util.CurrentUser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final ModelMapper modelMapper;
    private final UserService userService;
    private final TechnologyService technologyService;
    private final UserRepository userRepository;
    private final CurrentUser currentUser;

    public ProjectServiceImpl(ProjectRepository projectRepository, ModelMapper modelMapper, UserService userService, TechnologyService technologyService, UserRepository userRepository, CurrentUser currentUser) {
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.technologyService = technologyService;
        this.userRepository = userRepository;
        this.currentUser = currentUser;
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

        userEntity.addProjectForParticipant(projectEntity);

        userRepository.save(userEntity);
    }

    @Override
    public boolean isParticipant(Long id) {

        boolean isParticipant;

        ProjectEntity projectEntity = projectRepository.findById(id).get();
        UserEntity userEntity = userRepository.findById(currentUser.getId()).get();

        List<ProjectEntity> test =  projectRepository.findUserParticipateInProject(userEntity.getId(), projectEntity.getId());

        if ( test.isEmpty())
        {

            isParticipant = true;
        }
        else
        {
            isParticipant = false;
        }

        return isParticipant;
    }
}
