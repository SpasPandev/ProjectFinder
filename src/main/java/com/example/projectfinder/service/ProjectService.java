package com.example.projectfinder.service;

import com.example.projectfinder.model.service.ProjectServiceModel;
import com.example.projectfinder.model.view.ProjectViewModel;

import java.util.List;

public interface ProjectService {

    ProjectServiceModel findProjectId(Long id);

    List<ProjectViewModel> findAllProjectViews();
}