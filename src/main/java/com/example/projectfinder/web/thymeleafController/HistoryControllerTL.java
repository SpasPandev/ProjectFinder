package com.example.projectfinder.web.thymeleafController;

import com.example.projectfinder.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HistoryControllerTL {

    private final ProjectService projectService;

    public HistoryControllerTL(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/history")
    public String history(Model model) {

        model.addAttribute("listOfAllDeletedProjects", projectService.findAllDeletedProjects());
        return "history";
    }
}
