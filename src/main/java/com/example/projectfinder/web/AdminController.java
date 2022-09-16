package com.example.projectfinder.web;

import com.example.projectfinder.model.binding.ChangeRoleBindingModel;
import com.example.projectfinder.model.service.UserServiceModel;
import com.example.projectfinder.service.UserService;
import com.example.projectfinder.util.CurrentUser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AdminController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final CurrentUser currentUser;

    public AdminController(UserService userService, ModelMapper modelMapper, CurrentUser currentUser) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.currentUser = currentUser;
    }

    @ModelAttribute
    public ChangeRoleBindingModel changeRoleBindingModel() {
        return new ChangeRoleBindingModel();
    }

    @GetMapping("/admin")
    public String adminPanel(Model model)
    {
        if (currentUser.getId() == null)
        {
            return "redirect:/login";
        }

        model.addAttribute("isAdmin", userService.isAdmin(currentUser));

        model.addAttribute("allUsers", this.userService.findAllUsers());

        return "admin";
    }

    @PostMapping("/admin/{id}")
    public String adminChangeUserRole(@PathVariable Long id, @Valid ChangeRoleBindingModel changeRoleBindingModel,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            redirectAttributes
                    .addFlashAttribute("changeRoleBindingModel", changeRoleBindingModel);

            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.changeRoleBindingModel",
                            bindingResult);

            return "redirect:/admin";
        }

        userService.adminChangeUserRole(modelMapper.map(changeRoleBindingModel, UserServiceModel.class), id);

        return "redirect:/admin";
    }
}
