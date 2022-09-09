package com.example.projectfinder.web;

import com.example.projectfinder.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.validation.Valid;

@Controller
public class HomeController {

    private final ProjectService projectService;

    public HomeController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/home")
    public String home(Model model)
    {
        model.addAttribute("projectsList", this.projectService.findAllProjectViews());

        return "home";
    }

}
