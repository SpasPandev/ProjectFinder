package com.example.projectfinder.web;

import com.example.projectfinder.model.binding.EditProfileBindingModel;
import com.example.projectfinder.model.binding.UserRegisterBindingModel;
import com.example.projectfinder.model.service.EditProfileServiceModel;
import com.example.projectfinder.model.service.UserServiceModel;
import com.example.projectfinder.model.view.EditProfileViewModel;
import com.example.projectfinder.model.view.UserViewModel;
import com.example.projectfinder.service.UserService;
import com.example.projectfinder.service.impl.ApplicationUser;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @ModelAttribute
    public UserRegisterBindingModel userRegisterBindingModel() {
        return new UserRegisterBindingModel();
    }

    @GetMapping("/login")
    public String login() {

        return "login";
    }

    @PostMapping("/login-error")
    public String failedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY) String password,
            RedirectAttributes redirectAttributes) {

        Optional<UserServiceModel> userOpt = userService.findUserByUsername(username);

        if (userOpt.isPresent() && userOpt.get().isDeleted() &&
                passwordEncoder.matches(password, userOpt.get().getPassword())) {

            redirectAttributes.addFlashAttribute("showErrorMessDeletedUser", true);
            return "redirect:/login";
        }

        redirectAttributes.addFlashAttribute("showErrorMess", true);

        return "redirect:/login";
    }

    @GetMapping("/register")
    public String register() {

        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid UserRegisterBindingModel userRegisterBindingModel,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        boolean isUsernameExists = userService.isUsernameExists(userRegisterBindingModel.getUsername());

        boolean isEmailExists = userService.isEmailExists(userRegisterBindingModel.getEmail());

        boolean isChosenTechnologyListEmpty = userRegisterBindingModel.getTechnology().isEmpty();

        if (isUsernameExists && isEmailExists) {
            redirectAttributes
                    .addFlashAttribute("showUsernameExistsError", true)
                    .addFlashAttribute("showEmailExistsError", true)
                    .addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);

            return "redirect:/register";
        } else if (isUsernameExists) {

            redirectAttributes.addFlashAttribute("showUsernameExistsError", true);

            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);

            return "redirect:/register";

        } else if (isEmailExists) {

            redirectAttributes.addFlashAttribute("showEmailExistsError", true);

            redirectAttributes.addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);

            return "redirect:/register";
        }

        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {

            redirectAttributes
                    .addFlashAttribute("showPasswordsDontMatchError", true)
                    .addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);

            return "redirect:/register";
        }

        if (bindingResult.hasErrors() || isChosenTechnologyListEmpty) {

            redirectAttributes
                    .addFlashAttribute("isChosenTechnologyListEmpty", isChosenTechnologyListEmpty);

            redirectAttributes
                    .addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);

            redirectAttributes
                    .addFlashAttribute("isChosenTechnologyListEmpty", isChosenTechnologyListEmpty);

            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel",
                            bindingResult);

            return "redirect:/register";
        }

        userService.registerUser(userRegisterBindingModel);

        return "redirect:login";
    }

    @GetMapping("/profile/{id}")
    private String profile(@PathVariable Long id, @AuthenticationPrincipal ApplicationUser currentUser,
                           Model model) {

        model
                .addAttribute("technologyNameInString", userService.findUserTechnologyNameInString(id))
                .addAttribute("userRoleNameInString", userService.findUserRoleNameInString(id));

        model
                .addAttribute("user", modelMapper
                        .map(userService.findUserById(id), UserViewModel.class));

        model.addAttribute("currentUserId", currentUser.getId());

        model.addAttribute("currentUserRoleNameInString",
                userService.findUserRoleNameInString(currentUser.getId()));

        return "profile";
    }

    @GetMapping("/profile/{id}/editProfile")
    public String editProfile(@PathVariable Long id, @AuthenticationPrincipal ApplicationUser currentUser,
                              Model model) {

        if (!id.equals(currentUser.getId())) {
            return "redirect:/home";
        }

        EditProfileViewModel editProfileViewModel = this.userService.getById(id);
        EditProfileBindingModel editProfileBindingModel = modelMapper.map(editProfileViewModel, EditProfileBindingModel.class);

        model.addAttribute("editProfileBindingModel", editProfileBindingModel);

        return "editProfile";
    }

    @PatchMapping("/profile/{id}/editProfile")
    public String editProfileConfirm(@PathVariable Long id,
                                     @AuthenticationPrincipal ApplicationUser currentUser,
                                     @Valid EditProfileBindingModel editProfileBindingModel,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {

        boolean isChosenTechnologyListEmpty = editProfileBindingModel.getTechnology().isEmpty();

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("editProfileBindingModel", editProfileBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editProfileBindingModel", bindingResult);
            redirectAttributes.addFlashAttribute("isChosenTechnologyListEmpty", isChosenTechnologyListEmpty);

            return "redirect:/profile/" + id + "/editProfile";
        }

        if (isChosenTechnologyListEmpty) {
            redirectAttributes.addFlashAttribute("isChosenTechnologyListEmpty", isChosenTechnologyListEmpty);
            redirectAttributes.addFlashAttribute("editProfileBindingModel", editProfileBindingModel);

            return "redirect:/profile/" + id + "/editProfile";
        }

        boolean isUsernameExists = userService.isUsernameExists(editProfileBindingModel.getUsername());
        boolean isEmailExists = userService.isEmailExists(editProfileBindingModel.getEmail());

        if (isUsernameExists && !currentUser.getUsername().equals(editProfileBindingModel.getUsername())) {
            redirectAttributes
                    .addFlashAttribute("showUsernameExistsError", true)
                    .addFlashAttribute("editProfileBindingModel", editProfileBindingModel);

            return "redirect:/profile/" + id + "/editProfile";
        } else if (isEmailExists && !currentUser.getEmail().equals(editProfileBindingModel.getEmail())) {
            redirectAttributes
                    .addFlashAttribute("showEmailExistsError", true)
                    .addFlashAttribute("editProfileBindingModel", editProfileBindingModel);

            return "redirect:/profile/" + id + "/editProfile";
        }

        EditProfileServiceModel editProfileServiceModel = modelMapper.map(editProfileBindingModel, EditProfileServiceModel.class);
        editProfileServiceModel.setId(id);
        editProfileServiceModel.setPassword(passwordEncoder.encode(editProfileBindingModel.getPassword()));

        this.userService.updateProfile(editProfileServiceModel);

        return "redirect:/profile/" + id;
    }

}
