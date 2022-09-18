package com.example.projectfinder.web;

import com.example.projectfinder.model.binding.CreateProjectBindingModel;
import com.example.projectfinder.model.binding.SubmitLinkBindingModel;
import com.example.projectfinder.model.service.ProjectServiceModel;
import com.example.projectfinder.model.service.UserServiceModel;
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
    private final CurrentUser currentUser;
    private final UserService userService;

    public ProjectController(ModelMapper modelMapper, ProjectService projectService, CurrentUser currentUser, UserService userService) {
        this.modelMapper = modelMapper;
        this.projectService = projectService;
        this.currentUser = currentUser;
        this.userService = userService;
    }

    @ModelAttribute
    public CreateProjectBindingModel createProjectBindingModel()
    {
        return new CreateProjectBindingModel();
    }

    @ModelAttribute
    public SubmitLinkBindingModel submitLinkBindingModel () { return new SubmitLinkBindingModel(); }

    @GetMapping("/project/{id}")
    public String project(@PathVariable Long id, Model model)
    {

        if(currentUser.getId() == null)
        {
            return "redirect:/login";
        }

        model.addAttribute("allParticipants", projectService.showAllParticipants(id));

        model
                .addAttribute("projectId", modelMapper
                        .map(projectService.findProjectId(id), ProjectViewModel.class));
        model
                .addAttribute("isParticipant", projectService.isParticipant(id));


        model.addAttribute("technologyNameInString",
                projectService.findProjectTechnologyNameInString(id));

        if (projectService.isParticipant(id) != true)
        {
            model.addAttribute("isSubmitted", projectService.isSubmitted(id));
        }

        model.addAttribute("currentUserRoleNameInString", userService.findUserRoleNameInString(currentUser.getId()));


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

        if (userService.findUserRoleNameInString(currentUser.getId()).equals("STUDENT"))
        {
            return "redirect:/home";
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

    @PostMapping("/submit/{id}")
    public String submitProject(@PathVariable Long id, @Valid SubmitLinkBindingModel submitLinkBindingModel,
                                BindingResult bindingResult, RedirectAttributes redirectAttributes)
    {
        if (bindingResult.hasErrors()) {

            redirectAttributes
                    .addFlashAttribute("submitLinkBindingModel", submitLinkBindingModel);

            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.submitLinkBindingModel",
                            bindingResult);

            return "redirect:/project/{id}";
        }

        projectService.submitLink(modelMapper.map(submitLinkBindingModel, UserServiceModel.class), id);


        return "redirect:/project/{id}";
    }

}
