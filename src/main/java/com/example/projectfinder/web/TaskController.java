package com.example.projectfinder.web;

import com.example.projectfinder.model.entity.ProjectEntity;
import com.example.projectfinder.service.ProjectService;
import com.example.projectfinder.service.UserService;
import com.example.projectfinder.util.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TaskController {

    private final ProjectService projectService;
    private final UserService userService;
    private final CurrentUser currentUser;

    public TaskController(ProjectService projectService, UserService userService, CurrentUser currentUser) {
        this.projectService = projectService;
        this.userService = userService;
        this.currentUser = currentUser;
    }

    @GetMapping("/tasks")
    public String tasks(Model model)
    {
        List<ProjectEntity> allProjectsForCurrentUser = projectService.showCurrentUserProjects();

        model.addAttribute("allProjectsForCurrentUser", allProjectsForCurrentUser);

        model.addAttribute("currentUserRoleNameInString", userService.findUserRoleNameInString(currentUser.getId()));

        model.addAttribute("allProjectsForAuthor", projectService.findAllProjectsForAuthor(currentUser.getId()));

        return "tasks";
    }
}
