package com.example.projectfinder.web;

import com.example.projectfinder.service.ProjectService;
import com.example.projectfinder.util.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HistoryController {

    private final CurrentUser currentUser;
    private final ProjectService projectService;

    public HistoryController(CurrentUser currentUser, ProjectService projectService) {
        this.currentUser = currentUser;
        this.projectService = projectService;
    }

    @GetMapping("/history")
    public String history(Model model)
    {
        if (currentUser.getId() == null)
        {
            return "redirect:/login";
        }

        model.addAttribute("listOfAllDeletedProjects", projectService.findAllDeletedProjects());
        return "history";
    }
}
