package com.example.projectfinder.web;

import com.example.projectfinder.model.view.ProjectViewModel;
import com.example.projectfinder.service.ProjectService;
import com.example.projectfinder.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TaskController {

    private final ProjectService projectService;
    private final UserService userService;

    public TaskController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping("/tasks")
    public String tasks(Model model)
    {
        List<ProjectViewModel> allProjectsForCurrentUser = projectService.showCurrentUserProjects();

        model.addAttribute("allProjectsForCurrentUser", allProjectsForCurrentUser);

//        TODO
//        model.addAttribute("currentUserRoleNameInString", userService.findUserRoleNameInString(currentUser.getId()));

//        TODO
//        model.addAttribute("allProjectsForAuthor", projectService.findAllProjectsForAuthor(currentUser.getId()));

        return "tasks";
    }
}
