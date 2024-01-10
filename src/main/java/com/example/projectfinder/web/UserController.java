package com.example.projectfinder.web;

import com.example.projectfinder.model.dto.EditProfileDto;
import com.example.projectfinder.model.dto.UserRegisterReqDto;
import com.example.projectfinder.model.dto.UserLoginDto;
import com.example.projectfinder.model.view.UserViewModel;
import com.example.projectfinder.service.UserService;
import com.example.projectfinder.service.ApplicationUser;
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
    public UserRegisterReqDto userRegisterReqDto() {
        return new UserRegisterReqDto();
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

        Optional<UserLoginDto> userLoginDtoOpt = userService.findUserByUsername(username);

        if (userLoginDtoOpt.isPresent() && userLoginDtoOpt.get().isDeleted() &&
                passwordEncoder.matches(password, userLoginDtoOpt.get().getPassword())) {

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
    public String register(@Valid UserRegisterReqDto userRegisterReqDto,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        boolean isUsernameExists = userService.isUsernameExists(userRegisterReqDto.getUsername());

        boolean isEmailExists = userService.isEmailExists(userRegisterReqDto.getEmail());

        boolean isChosenTechnologyListEmpty = userRegisterReqDto.getTechnology().isEmpty();

        if (isUsernameExists && isEmailExists) {
            redirectAttributes
                    .addFlashAttribute("showUsernameExistsError", true)
                    .addFlashAttribute("showEmailExistsError", true)
                    .addFlashAttribute("userRegisterReqDto", userRegisterReqDto);

            return "redirect:/register";
        } else if (isUsernameExists) {

            redirectAttributes.addFlashAttribute("showUsernameExistsError", true);

            redirectAttributes.addFlashAttribute("userRegisterReqDto", userRegisterReqDto);

            return "redirect:/register";

        } else if (isEmailExists) {

            redirectAttributes.addFlashAttribute("showEmailExistsError", true);

            redirectAttributes.addFlashAttribute("userRegisterReqDto", userRegisterReqDto);

            return "redirect:/register";
        }

        if (!userRegisterReqDto.getPassword().equals(userRegisterReqDto.getConfirmPassword())) {

            redirectAttributes
                    .addFlashAttribute("showPasswordsDontMatchError", true)
                    .addFlashAttribute("userRegisterReqDto", userRegisterReqDto);

            return "redirect:/register";
        }

        if (bindingResult.hasErrors() || isChosenTechnologyListEmpty) {

            redirectAttributes
                    .addFlashAttribute("isChosenTechnologyListEmpty", isChosenTechnologyListEmpty);

            redirectAttributes
                    .addFlashAttribute("userRegisterReqDto", userRegisterReqDto);

            redirectAttributes
                    .addFlashAttribute("isChosenTechnologyListEmpty", isChosenTechnologyListEmpty);

            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.userRegisterReqDto",
                            bindingResult);

            return "redirect:/register";
        }

        userService.registerUser(userRegisterReqDto);

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

        com.example.projectfinder.model.dto.EditProfileDto editProfileDto = userService.getEditProfileDtoById(id);

        model.addAttribute("editProfileDto", editProfileDto);

        return "editProfile";
    }

    @PatchMapping("/profile/{id}/editProfile")
    public String editProfileConfirm(@PathVariable Long id,
                                     @AuthenticationPrincipal ApplicationUser currentUser,
                                     @Valid EditProfileDto editProfileDto,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {

        boolean isChosenTechnologyListEmpty = editProfileDto.getTechnology().isEmpty();

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("editProfileDto", editProfileDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editProfileDto", bindingResult);
            redirectAttributes.addFlashAttribute("isChosenTechnologyListEmpty", isChosenTechnologyListEmpty);

            return "redirect:/profile/" + id + "/editProfile";
        }

        if (isChosenTechnologyListEmpty) {
            redirectAttributes.addFlashAttribute("isChosenTechnologyListEmpty", isChosenTechnologyListEmpty);
            redirectAttributes.addFlashAttribute("editProfileDto", editProfileDto);

            return "redirect:/profile/" + id + "/editProfile";
        }

        boolean isUsernameExists = userService.isUsernameExists(editProfileDto.getUsername());
        boolean isEmailExists = userService.isEmailExists(editProfileDto.getEmail());

        if (isUsernameExists && !currentUser.getUsername().equals(editProfileDto.getUsername())) {
            redirectAttributes
                    .addFlashAttribute("showUsernameExistsError", true)
                    .addFlashAttribute("editProfileDto", editProfileDto);

            return "redirect:/profile/" + id + "/editProfile";
        }
        else if (isEmailExists && !currentUser.getEmail().equals(editProfileDto.getEmail())) {
            redirectAttributes
                    .addFlashAttribute("showEmailExistsError", true)
                    .addFlashAttribute("editProfileDto", editProfileDto);

            return "redirect:/profile/" + id + "/editProfile";
        }

        this.userService.updateProfile(editProfileDto);

        return "redirect:/profile/" + id;
    }

}
