package com.example.projectfinder.web;

import com.example.projectfinder.model.view.ProjectViewModel;
import com.example.projectfinder.service.ProjectService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    private final ProjectService projectService;

    private final ModelMapper modelMapper;

    public HomeController(ProjectService projectService, ModelMapper modelMapper) {
        this.projectService = projectService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/home")
    public String home(Model model)
    {
        model.addAttribute("projectsList", this.projectService.findAllProjectViews());

        return "home";
    }

    @GetMapping("/project/{id}")
    public String project(@PathVariable Long id, Model model)
    {
        model
                .addAttribute("projectId", modelMapper
                        .map(projectService.findProjectId(id), ProjectViewModel.class));

        return "project";
    }
}
