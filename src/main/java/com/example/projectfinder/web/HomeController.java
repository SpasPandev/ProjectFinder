package com.example.projectfinder.web;

import com.example.projectfinder.service.ProjectService;
import com.example.projectfinder.service.UserService;
import com.example.projectfinder.service.impl.ApplicationUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    private final ProjectService projectService;
    private final UserService userService;

    private boolean interests = false;

    public HomeController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal ApplicationUser currentUser, Model model) {

        String currentUserRoleNameInString = userService.findUserRoleNameInString(currentUser.getId());

        if (currentUserRoleNameInString.equals("COMPANY")) {
            interests = false;
        }

        model.addAttribute("interests", interests);

        model.addAttribute("projectsList", this.projectService.findAllProjectViewsOrderDescId());

        if (!currentUserRoleNameInString.equals("COMPANY")) {
            model.addAttribute("allProjectsForConcreteTechnologies",
                    projectService.findAllProjectsForConcreteTechnologies(
                            userService.finsUserTechnologiesIdsByUserId(currentUser.getId())));

            model.addAttribute("currentUserTechnologyNameInString",
                    userService.findUserTechnologyNameInString(currentUser.getId()));
        }

        return "home";
    }

    @PostMapping("/home")
    public String changeViewOfPresentedProjects() {

        interests = !interests;

        return "redirect:/home";
    }

}
