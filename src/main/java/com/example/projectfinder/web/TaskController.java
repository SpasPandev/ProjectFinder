package com.example.projectfinder.web;

import com.example.projectfinder.model.view.ProjectViewModel;
import com.example.projectfinder.service.ProjectService;
import com.example.projectfinder.service.ApplicationUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TaskController {

    private final ProjectService projectService;

    public TaskController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/tasks")
    public String tasks(@AuthenticationPrincipal ApplicationUser currentUser, Model model) {

        List<ProjectViewModel> allProjectsForCurrentUser = projectService.showCurrentUserProjects(currentUser.getId());

        model.addAttribute("allProjectsForCurrentUser", allProjectsForCurrentUser);

        model.addAttribute("allProjectsForAuthor", projectService.findAllProjectsForAuthor(currentUser.getId()));

        return "tasks";
    }
}
