package com.example.projectfinder.web;

import com.example.projectfinder.repository.UserRepository;
import com.example.projectfinder.service.ProjectService;
import com.example.projectfinder.service.UserService;
import com.example.projectfinder.util.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {

    private final ProjectService projectService;
    private final UserService userService;
    private final CurrentUser currentUser;
    private final UserRepository userRepository;

    private boolean interests = false;

    public HomeController(ProjectService projectService, UserService userService, CurrentUser currentUser, UserRepository userRepository) {
        this.projectService = projectService;
        this.userService = userService;
        this.currentUser = currentUser;
        this.userRepository = userRepository;
    }

    @GetMapping("/home")
    public String home(Model model)
    {
        if (currentUser.getId() == null)
        {
            return "redirect:/login";
        }

        if (currentUser.getRoleName().equals("COMPANY")) {interests = false;}

        model.addAttribute("interests", interests);
        
        model.addAttribute("projectsList", this.projectService.findAllProjectViewsOrderDescId());

        if (!userService.findUserRoleNameInString(currentUser.getId()).equals("COMPANY"))
        {
            model.addAttribute("allProjectsForConcretTehnology",
                    projectService.findAllProjectsForConcretTehnology(
                            userRepository.findTechnologyIdsByUserId(currentUser.getId())));

            model.addAttribute("currentUserTechnologyNameInString",
                    userService.findUserTechnologyNameInString(currentUser.getId()));
        }

        model.addAttribute("currentUserRoleInString",
                userService.findUserRoleNameInString(currentUser.getId()));

        return "home";
    }

    @PostMapping("/home")
    public String changeViewOfPresentedProjects(Model model)
    {
        if (interests == false)
        {
            interests = true;
        }
        else
        {
            interests = false;
        }

        return "redirect:/home";
    }

}
