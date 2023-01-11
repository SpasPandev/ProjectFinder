package com.example.projectfinder.web;

import com.example.projectfinder.model.binding.EditProfileBindingModel;
import com.example.projectfinder.model.binding.UserLoginBindingModel;
import com.example.projectfinder.model.binding.UserRegisterBindingModel;
import com.example.projectfinder.model.service.EditProfileServiceModel;
import com.example.projectfinder.model.service.UserServiceModel;
import com.example.projectfinder.model.view.EditProfileViewModel;
import com.example.projectfinder.model.view.UserViewModel;
import com.example.projectfinder.repository.UserRepository;
import com.example.projectfinder.service.UserService;
import com.example.projectfinder.util.CurrentUser;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserController(UserService userService, ModelMapper modelMapper, CurrentUser currentUser, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.currentUser = currentUser;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
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
        if (currentUser.getId() != null)
        {
            return "redirect:/home";
        }

        return "login";
    }

    @PostMapping("/login")
    public String loginConfirm(@Valid UserLoginBindingModel userLoginBindingModel,
                               BindingResult bindingResult, RedirectAttributes redirectAttributes)
    {
        if (bindingResult.hasErrors() )
        {
            redirectAttributes
                    .addFlashAttribute("userLoginBindingModel", userLoginBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userLoginBindingModel",
                            bindingResult);

            return "redirect:/login";
        }

        if(userRepository.findByUsername(userLoginBindingModel.getUsername()).isEmpty() ||
                passwordEncoder.matches(userLoginBindingModel.getPassword(), userRepository.findByUsername(userLoginBindingModel.getUsername()).get().getPassword()) == false)
        {
            redirectAttributes
                    .addFlashAttribute("showErrorMess", true)
                    .addFlashAttribute("userLoginBindingModel", userLoginBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userLoginBindingModel",
                            bindingResult);
            return "redirect:/login";
        }

        UserServiceModel user = userService.findUserByUsername(userLoginBindingModel.getUsername());

        if  (user.isDeleted())
        {
            redirectAttributes
                    .addFlashAttribute("showErrorMessDeletedUser", true)
                    .addFlashAttribute("userLoginBindingModel", userLoginBindingModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userLoginBindingModel",
                            bindingResult);
            return "redirect:/login";
        }

        userService.loginUser(user.getId(), user.getUsername());

        return "redirect:/home";
    }

    @GetMapping("/register")
    public String register() {

        if (currentUser.getId() != null)
        {
            return "redirect:/home";
        }

        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid UserRegisterBindingModel userRegisterBindingModel,
                           BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        boolean isUsernameExists = userService.isUsernameExists(userRegisterBindingModel.getUsername());

        boolean isEmailExists = userService.isEmailExists(userRegisterBindingModel.getEmail());

        boolean isChoosenTechnologyListEmpty = userRegisterBindingModel.getTechnology().isEmpty();

        if (isUsernameExists && isEmailExists)
        {
            redirectAttributes
                    .addFlashAttribute("showUsernameExistsError", true)
                    .addFlashAttribute("showEmailExistsError", true)
                    .addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);

            return "redirect:/register";
        }
        else if (isUsernameExists)
        {
            redirectAttributes
                    .addFlashAttribute("showUsernameExistsError", true);

            redirectAttributes
                    .addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);

            return "redirect:/register";
        }
        else if (isEmailExists)
        {
            redirectAttributes
                    .addFlashAttribute("showEmailExistsError", true);

            redirectAttributes
                    .addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);

            return "redirect:/register";
        }

        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword()))
        {

            redirectAttributes
                    .addFlashAttribute("showPasswordsDontMatchError", true)
                    .addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);

            return "redirect:/register";
        }

        if (bindingResult.hasErrors() || isChoosenTechnologyListEmpty ) {

            redirectAttributes
                    .addFlashAttribute("isChoosenTechnologyListEmpty", isChoosenTechnologyListEmpty);

            redirectAttributes
                    .addFlashAttribute("userRegisterBindingModel", userRegisterBindingModel);

            redirectAttributes
                    .addFlashAttribute("isChoosenTechnologyListEmpty", isChoosenTechnologyListEmpty);

            redirectAttributes
                    .addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel",
                            bindingResult);

            return "redirect:/register";
        }

        userService.registerUser(modelMapper.map(userRegisterBindingModel, UserServiceModel.class));

        return "redirect:login";
    }

    @GetMapping("/logout")
    public String logout()
    {
        userService.logoutUser();

        return "redirect:/login";
    }

    @GetMapping("/profile/{id}")
    private String profile(@PathVariable Long id, Model model)
    {
        if (currentUser.getId() == null)
        {
            return "redirect:/login";
        }

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
        EditProfileBindingModel editProfileBindingModel = modelMapper.map(editProfileViewModel, EditProfileBindingModel.class);

        model.addAttribute("editProfileBindingModel", editProfileBindingModel);

        model.addAttribute("currentUserId", currentUser.getId());

        model.addAttribute("currentUserRoleNameInString",
                userService.findUserRoleNameInString(currentUser.getId()));

        return "editProfile";
    }
    @GetMapping("/profile/{id}/editProfile/errors")
    public String editProfileErrors(@PathVariable Long id, Model model)
    {
        if (id != currentUser.getId())
        {
            return "redirect:/home";
        }

        if (currentUser.getId() == null)
        {
            return "redirect:/login";
        }

        model.addAttribute("currentUserRoleNameInString",
                userService.findUserRoleNameInString(currentUser.getId()));

        return "editProfile";
    }

    @PatchMapping("/profile/{id}/editProfile")
    public String editProfileConfirm(@PathVariable Long id,
                                     @Valid EditProfileBindingModel editProfileBindingModel,
                                     BindingResult bindingResult,
                                     RedirectAttributes redirectAttributes) {

        boolean isChoosenTechnologyListEmpty = editProfileBindingModel.getTechnology().isEmpty();

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("editProfileBindingModel", editProfileBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editProfileBindingModel", bindingResult);
            redirectAttributes.addFlashAttribute("isChoosenTechnologyListEmpty", isChoosenTechnologyListEmpty);

            return "redirect:/profile/" + id + "/editProfile/errors";
        }

        if (isChoosenTechnologyListEmpty)
        {
            redirectAttributes.addFlashAttribute("isChoosenTechnologyListEmpty", isChoosenTechnologyListEmpty);
            redirectAttributes.addFlashAttribute("editProfileBindingModel", editProfileBindingModel);

            return "redirect:/profile/" + id + "/editProfile/errors";
        }

        boolean isUsernameExists = userService.isUsernameExists(editProfileBindingModel.getUsername());
        boolean isEmailExists = userService.isEmailExists(editProfileBindingModel.getEmail());

        if (isUsernameExists && !currentUser.getUsername().equals(editProfileBindingModel.getUsername()))
        {
            redirectAttributes
                    .addFlashAttribute("showUsernameExistsError", true)
                    .addFlashAttribute("editProfileBindingModel", editProfileBindingModel);

            return "redirect:/profile/" + id + "/editProfile/errors";
        }
        else if (isEmailExists && !currentUser.getEmail().equals(editProfileBindingModel.getEmail()))
        {
            redirectAttributes
                    .addFlashAttribute("showEmailExistsError", true)
                    .addFlashAttribute("editProfileBindingModel", editProfileBindingModel);

            return "redirect:/profile/" + id + "/editProfile/errors";
        }

        EditProfileServiceModel editProfileServiceModel = modelMapper.map(editProfileBindingModel, EditProfileServiceModel.class);
        editProfileServiceModel.setId(id);
        editProfileServiceModel.setPassword(passwordEncoder.encode(editProfileBindingModel.getPassword()));

        this.userService.updateProfile(editProfileServiceModel);

        return "redirect:/profile/" + id;
    }

}
