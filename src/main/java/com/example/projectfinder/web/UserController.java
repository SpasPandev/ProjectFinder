package com.example.projectfinder.web;

import com.example.projectfinder.model.binding.EditProejectBindingModel;
import com.example.projectfinder.model.binding.UserLoginBindingModel;
import com.example.projectfinder.model.binding.UserRegisterBindingModel;
import com.example.projectfinder.model.service.EditProfileServiceModel;
import com.example.projectfinder.model.service.UserServiceModel;
import com.example.projectfinder.model.view.EditProfileViewModel;
import com.example.projectfinder.model.view.UserViewModel;
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
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final CurrentUser currentUser;

    public UserController(UserService userService, ModelMapper modelMapper, CurrentUser currentUser) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.currentUser = currentUser;
    }

    @ModelAttribute
    public UserRegisterBindingModel userRegisterBindingModel() {
        return new UserRegisterBindingModel();
    }

    @ModelAttribute
    public UserLoginBindingModel userLoginBindingModel()
    {
        return new UserLoginBindingModel();
    }

    @GetMapping("/login")
    public String login(Model model)
    {
        model.addAttribute("isExists", true);
        return "login";
    }

    @PostMapping("/login")
    public String loginConfirm( UserLoginBindingModel userLoginBindingModel,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes)
    {
        if (bindingResult.hasErrors())
        {
            redirectAttributes
                    .addFlashAttribute("userLoginBindingModel", userLoginBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userLoginBindingModel",
                            bindingResult);

            return "redirect:login";
        }

        UserServiceModel user = userService.findUserByUsernameAndPassword(userLoginBindingModel.getUsername(),
                userLoginBindingModel.getPassword());

        if (user == null)
        {
            redirectAttributes
                    .addFlashAttribute("isExists", false)
                    .addFlashAttribute("UserLoginBindingModel", userLoginBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userLoginBindingModel",
                            bindingResult);

            return "redirect:login";
        }

        userService.loginUser(user.getId(), user.getUsername());

        return "redirect:home";
    }

    @GetMapping("/register")
    public String register(Model model) {

        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid UserRegisterBindingModel userRegisterBindingModel,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors() || !userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {

            redirectAttributes
                    .addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);

            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel",
                            bindingResult);

            return "redirect:/register";
        }

        boolean isUsernameExists = userService.isUsernameExists(userRegisterBindingModel.getUsername());

        if (isUsernameExists)
        {
            ///TODO
        }

        userService.registerUser(modelMapper.map(userRegisterBindingModel, UserServiceModel.class));

        return "redirect:login";
    }

    @GetMapping("/profile/{id}")
    private String profile(@PathVariable Long id, Model model)
    {
        if (currentUser.getId() == null)
        {
            return "redirect:/login";
        }

        if (!userService.findUserRoleNameInString(currentUser.getId()).equals("COMPANY"))
        {
            model.addAttribute("technologyNameInString",
                    userService.findUserTechnologyNameInString(id));
        }

        model
                .addAttribute("user", modelMapper
                        .map(userService.findUserById(id), UserViewModel.class));

        model.addAttribute("currentUserId", currentUser.getId());

        model.addAttribute("currentUserRoleNameInString",
                userService.findUserRoleNameInString(currentUser.getId()));

        return "profile";
    }

    @GetMapping("/profile/{id}/editProfile")
    public String editProfile(@PathVariable Long id, Model model)
    {
        if (id != currentUser.getId())
        {
            return "redirect:/home";
        }

        if (currentUser.getId() == null)
        {
            return "redirect:/login";
        }

        EditProfileViewModel editProfileViewModel = this.userService.getById(id);
        EditProejectBindingModel editProejectBindingModel = modelMapper.map(editProfileViewModel, EditProejectBindingModel.class);

        model.addAttribute("editProejectBindingModel", editProejectBindingModel);

        model.addAttribute("currentUserId", currentUser.getId());

        model.addAttribute("currentUserRoleNameInString",
                userService.findUserRoleNameInString(currentUser.getId()));

        return "editProfile";
    }
    @GetMapping("/profile/{id}/editProfile/errors")
    public String editProfileErrors(@PathVariable Long id, Model model)
    {

        return "editProfile";
    }

    @PatchMapping("/profile/{id}/editProfile")
    public String editProfileConfirm(@PathVariable Long id,
                                     @Valid EditProejectBindingModel editProejectBindingModel,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("editProejectBindingModel", editProejectBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editProejectBindingModel", bindingResult);

            return "redirect:/profile/" + id + "/editProfile/errors";
        }

        EditProfileServiceModel editProfileServiceModel = modelMapper.map(editProejectBindingModel, EditProfileServiceModel.class);
        editProfileServiceModel.setId(id);

        this.userService.updateProfile(editProfileServiceModel);


        return "redirect:/profile/" + id;
    }

}
