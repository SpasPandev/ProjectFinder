package com.example.projectfinder.web;

import com.example.projectfinder.service.ProjectService;
import com.example.projectfinder.util.CurrentUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UploadedController {

    private final ProjectService projectService;
    private final CurrentUser currentUser;

    public UploadedController(ProjectService projectService, CurrentUser currentUser) {
        this.projectService = projectService;
        this.currentUser = currentUser;
    }

    @GetMapping("/uploaded/{id}")
    public String uploaded(@PathVariable Long id, Model model)
    {
        if (currentUser.getId() == null)
        {
            return "redirect:/login";
        }

        model.addAttribute("currentProject", projectService.findProjectById(id));
        model.addAttribute("listOfAllProjectParticipantsUploadedOnCurrentProject", projectService.currentProjectUploaders(id));

        return "uploaded";
    }
}
