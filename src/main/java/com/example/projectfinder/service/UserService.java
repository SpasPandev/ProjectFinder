package com.example.projectfinder.service;

import com.example.projectfinder.model.entity.UserEntity;
import com.example.projectfinder.model.service.EditProfileServiceModel;
import com.example.projectfinder.model.service.UserServiceModel;
import com.example.projectfinder.model.view.EditProfileViewModel;
import com.example.projectfinder.model.view.UserViewModel;
import com.example.projectfinder.util.CurrentUser;

import java.util.List;

public interface UserService {
    void registerUser(UserServiceModel userServiceModel);

    UserServiceModel findUserByUsername(String username);

    void loginUser(Long id, String username);

    void logoutUser();

    UserServiceModel findUserById(Long id);

    boolean isUsernameExists(String username);

    UserEntity findCurrentLoginUserEntity();

    EditProfileViewModel getById(Long id);

    void updateProfile(EditProfileServiceModel editProfileServiceModel);

    List<UserViewModel> findAllUsers();

    String findUserTechnologyNameInString(Long currentUserId);

    void adminChangeUserRole(UserServiceModel userServiceModel, Long id);

    boolean isAdmin(CurrentUser currentUser);

    String findUserRoleNameInString(Long currentUserId);
}
