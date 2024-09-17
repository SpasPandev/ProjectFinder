package com.example.projectfinder.web.thymeleafController;

import com.example.projectfinder.service.ProjectService;
import com.example.projectfinder.service.ApplicationUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UploadedControllerTL {

    private final ProjectService projectService;

    public UploadedControllerTL(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/uploaded/{id}")
    public String uploaded(@PathVariable Long id, @AuthenticationPrincipal ApplicationUser currentUser,
                           Model model) {

        if (!projectService.isAuthor(id, currentUser.getId())) {
            return "redirect:/home";
        }

        model.addAttribute("currentProject", projectService.findProjectById(id));
        model.addAttribute("listOfAllProjectParticipantsUploadedOnCurrentProject", projectService.currentProjectUploaders(id));

        return "uploaded";
    }
}
