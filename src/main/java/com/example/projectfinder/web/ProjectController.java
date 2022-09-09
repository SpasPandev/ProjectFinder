package com.example.projectfinder.web;

import com.example.projectfinder.model.binding.CreateProjectBindingModel;
import com.example.projectfinder.model.service.ProjectServiceModel;
import com.example.projectfinder.model.view.ProjectViewModel;
import com.example.projectfinder.service.ProjectService;
import com.example.projectfinder.service.UserService;
import com.example.projectfinder.util.CurrentUser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class ProjectController {

    private final ModelMapper modelMapper;
    private final ProjectService projectService;
    private final UserService userService;
    private final CurrentUser currentUser;

    public ProjectController(ModelMapper modelMapper, ProjectService projectService, UserService userService, CurrentUser currentUser) {
        this.modelMapper = modelMapper;
        this.projectService = projectService;
        this.userService = userService;
        this.currentUser = currentUser;
    }

    @ModelAttribute
    public CreateProjectBindingModel createProjectBindingModel()
    {
        return new CreateProjectBindingModel();
    }

    @GetMapping("/project/{id}")
    public String project(@PathVariable Long id, Model model)
    {

        if(currentUser.getId() == null)
        {
            return "redirect:/login";
        }

        model
                .addAttribute("projectId", modelMapper
                        .map(projectService.findProjectId(id), ProjectViewModel.class));
        model
                .addAttribute("isParticipant", projectService.isParticipant(id));

        model
                .addAttribute("user", userService.findUserById(currentUser.getId()));

        return "project";
    }

    @PostMapping("/project/{id}")
    public String participate(@PathVariable Long id)
    {
        projectService.participateInProject(id);

        return "redirect:/home";
    }


    @GetMapping("/createProject")
    public String createProject()
    {
        if(currentUser.getId() == null)
        {
            return "redirect:/login";
        }

        return "createProject";
    }

    @PostMapping("/createProject")
    public String createProjectConfirm(@Valid CreateProjectBindingModel createProjectBindingModel, BindingResult bindingResult, RedirectAttributes redirectAttributes)
    {
        if (bindingResult.hasErrors())
        {
            redirectAttributes.addFlashAttribute("createProjectBindingModel", createProjectBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.createProjectBindingModel", bindingResult);

            return "redirect:createProject";
        }

        ProjectServiceModel projectServiceModel = modelMapper
                .map(createProjectBindingModel, ProjectServiceModel.class);

        projectService.createNewProject(projectServiceModel);

        return "redirect:home";
    }

}
