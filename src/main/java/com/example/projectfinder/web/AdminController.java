package com.example.projectfinder.web;

import com.example.projectfinder.model.binding.ChangeRoleBindingModel;
import com.example.projectfinder.model.service.UserServiceModel;
import com.example.projectfinder.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AdminController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    public AdminController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @ModelAttribute
    public ChangeRoleBindingModel changeRoleBindingModel() {
        return new ChangeRoleBindingModel();
    }

    @GetMapping("/admin")
    public String adminPanel(Model model)
    {

//        TODO
//        if (!userService.findUserRoleNameInString(currentUser.getId()).equals("ADMIN"))
//        {
//            return "redirect:/home";
//        }

//        TODO
//        model.addAttribute("isAdmin", userService.isAdmin(currentUser));

//        TODO
//        model.addAttribute("currentUserId", currentUser.getId());

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

    @Transactional
    @PatchMapping("/admin/{id}")
    public String deleteUser(@PathVariable Long id)
    {
//        userService.deleteUserById(id);
        userService.setIsDeleatedStatusTrue(id);

        return "redirect:/admin";
    }
}
