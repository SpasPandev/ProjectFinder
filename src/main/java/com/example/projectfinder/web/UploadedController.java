package com.example.projectfinder.web;

import com.example.projectfinder.service.ProjectService;
import com.example.projectfinder.service.impl.ApplicationUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UploadedController {

    private final ProjectService projectService;

    public UploadedController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/uploaded/{id}")
    public String uploaded(@PathVariable Long id, @AuthenticationPrincipal ApplicationUser currentUser,
                           Model model)
    {

        if  (!projectService.isAuthor(id, currentUser.getId()))
        {
            return "redirect:/home";
        }

        model.addAttribute("currentProject", projectService.findProjectById(id));
        model.addAttribute("listOfAllProjectParticipantsUploadedOnCurrentProject", projectService.currentProjectUploaders(id));

        return "uploaded";
    }
}
