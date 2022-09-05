package com.example.projectfinder.service.impl;

import com.example.projectfinder.model.entity.ProjectEntity;
import com.example.projectfinder.model.service.ProjectServiceModel;
import com.example.projectfinder.model.view.ProjectViewModel;
import com.example.projectfinder.repository.ProjectRepository;
import com.example.projectfinder.service.ProjectService;
import com.example.projectfinder.service.TechnologyService;
import com.example.projectfinder.service.UserService;
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

    public ProjectServiceImpl(ProjectRepository projectRepository, ModelMapper modelMapper, UserService userService, TechnologyService technologyService) {
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.technologyService = technologyService;
    }

    @Override
    public ProjectServiceModel findProjectId(Long id) {
        return projectRepository
                .findById(id)
                .map(projectEntity -> modelMapper.map(projectEntity, ProjectServiceModel.class))
                .orElse(null);

    }

    @Override
    public List<ProjectViewModel> findAllProjectViews() {
        return this.projectRepository.findAll()
                .stream().map(projectEntity -> {
                    ProjectViewModel projectViewModel = modelMapper.map(projectEntity, ProjectViewModel.class);

                    return projectViewModel;
                })
                .collect(Collectors.toList());
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
}
