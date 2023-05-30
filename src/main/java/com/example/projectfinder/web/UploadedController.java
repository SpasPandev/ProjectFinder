package com.example.projectfinder.web;

import com.example.projectfinder.service.ProjectService;
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
    public String uploaded(@PathVariable Long id, Model model)
    {
//        TODO
//        if  (currentUser.getId() != projectService.findProjectAuthorId(id))
//        {
//            return "redirect:/home";
//        }

        model.addAttribute("currentProject", projectService.findProjectById(id));
        model.addAttribute("listOfAllProjectParticipantsUploadedOnCurrentProject", projectService.currentProjectUploaders(id));

        return "uploaded";
    }
}
