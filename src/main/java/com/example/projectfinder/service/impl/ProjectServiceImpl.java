package com.example.projectfinder.service.impl;

import com.example.projectfinder.model.service.ProjectServiceModel;
import com.example.projectfinder.model.view.ProjectViewModel;
import com.example.projectfinder.repository.ProjectRepository;
import com.example.projectfinder.service.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    private final ModelMapper modelMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ModelMapper modelMapper) {
        this.projectRepository = projectRepository;
        this.modelMapper = modelMapper;
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
}
