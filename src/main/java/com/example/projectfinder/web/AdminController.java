package com.example.projectfinder.web;

import com.example.projectfinder.model.dto.ChangeRoleDto;
import com.example.projectfinder.model.service.UserServiceModel;
import com.example.projectfinder.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ChangeRoleDto changeRoleDto() {
        return new ChangeRoleDto();
    }

    @GetMapping("/admin")
    public String adminPanel(Model model) {

        model.addAttribute("allUsers", this.userService.findAllUsers());

        return "admin";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/{id}")
    public String adminChangeUserRole(@PathVariable Long id, @Valid ChangeRoleDto changeRoleDto,
                                      BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            redirectAttributes
                    .addFlashAttribute("changeRoleDto", changeRoleDto);

            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.changeRoleDto",
                            bindingResult);

            return "redirect:/admin";
        }

        userService.adminChangeUserRole(changeRoleDto, id);

        return "redirect:/admin";
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/admin/{id}")
    public String deleteUser(@PathVariable Long id) {

//        userService.deleteUserById(id);
        userService.setIsDeletedStatusTrue(id);

        return "redirect:/admin";
    }
}
