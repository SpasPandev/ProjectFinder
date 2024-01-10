package com.example.projectfinder.web;

import com.example.projectfinder.model.dto.CreateProjectDto;
import com.example.projectfinder.model.binding.SubmitLinkBindingModel;
import com.example.projectfinder.model.service.ProjectServiceModel;
import com.example.projectfinder.model.service.UserServiceModel;
import com.example.projectfinder.service.ApplicationUser;
import com.example.projectfinder.service.ProjectService;
import com.example.projectfinder.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class ProjectController {

    private final ModelMapper modelMapper;
    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ModelMapper modelMapper, ProjectService projectService, UserService userService) {
        this.modelMapper = modelMapper;
        this.projectService = projectService;
        this.userService = userService;
    }

    @ModelAttribute
    public CreateProjectDto createProjectDto() {
        return new CreateProjectDto();
    }

    @ModelAttribute
    public SubmitLinkBindingModel submitLinkBindingModel() {
        return new SubmitLinkBindingModel();
    }

    @GetMapping("/project/{id}")
    public String project(@PathVariable Long id, @AuthenticationPrincipal ApplicationUser currentUser,
                          Model model) {
        boolean isParticipant = projectService.isParticipant(id, currentUser.getId());

        model.addAttribute("allParticipants", projectService.showAllParticipants(id));

        model.addAttribute("project",
                projectService.findProjectViewModelByProjectId(id));

        model.addAttribute("isParticipant", isParticipant);

        model.addAttribute("technologyNameInString",
                projectService.findProjectTechnologyNameInString(id));

        if (isParticipant) {
            model.addAttribute("isSubmitted", projectService.isSubmitted(id, currentUser.getId()));
        }

        model.addAttribute("currentUserRoleNameInString", userService.findUserRoleNameInString(currentUser.getId()));

        boolean isAuthor = projectService.isAuthor(id, currentUser.getId());

        model.addAttribute("isAuthor", isAuthor);

        return "project";
    }

    @PostMapping("/project/{id}")
    public String participate(@PathVariable Long id, @AuthenticationPrincipal ApplicationUser currentUser) {

        projectService.participateInProject(id, currentUser.getId());

        return "redirect:/project/{id}";
    }

    @PreAuthorize("!hasRole('STUDENT')")
    @GetMapping("/createProject")
    public String createProject() {

        return "createProject";
    }

    @PreAuthorize("!hasRole('STUDENT')")
    @PostMapping("/createProject")
    public String createProjectConfirm(@Valid CreateProjectDto createProjectDto,
                                       BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                       @AuthenticationPrincipal ApplicationUser currentUser) {

        boolean isChosenTechnologyListEmpty = createProjectDto.getTechnologies().isEmpty();

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("isChosenTechnologyListEmpty", isChosenTechnologyListEmpty);
            redirectAttributes.addFlashAttribute("createProjectDto", createProjectDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.createProjectDto", bindingResult);

            return "redirect:createProject";
        }

        projectService.createNewProject(createProjectDto, currentUser.getId());

        return "redirect:home";
    }

    @PostMapping("/submit/{id}")
    public String submitProject(@PathVariable Long id, @Valid SubmitLinkBindingModel submitLinkBindingModel,
                                BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal ApplicationUser currentUser) {

        Pattern pattern = Pattern.compile("^(https?:\\/\\/github.com?\\/)\\w*(\\/)\\w*");
        Matcher matcher = pattern.matcher(submitLinkBindingModel.getLink());

        if (!matcher.matches()) {

            redirectAttributes.addFlashAttribute("isLinkNotCorrect", true);

            return "redirect:/project/{id}";
        }

        if (bindingResult.hasErrors()) {

            redirectAttributes.addFlashAttribute("submitLinkBindingModel", submitLinkBindingModel);

            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.submitLinkBindingModel",
                    bindingResult);

            return "redirect:/project/{id}";
        }

        projectService.submitLink(modelMapper.map(submitLinkBindingModel, UserServiceModel.class),
                id, currentUser.getId());

        return "redirect:/project/{id}";
    }

}
