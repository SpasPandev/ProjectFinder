package com.example.projectfinder.service;

import com.example.projectfinder.model.entity.UserEntity;
import com.example.projectfinder.model.service.EditProfileServiceModel;
import com.example.projectfinder.model.service.UserServiceModel;
import com.example.projectfinder.model.view.EditProfileViewModel;

public interface UserService {
    void registerUser(UserServiceModel userServiceModel);

    UserServiceModel findUserByUsernameAndPassword(String username, String password);

    void loginUser(Long id, String username);

    UserServiceModel findUserById(Long id);

    boolean isNameExists(String username);

    UserEntity findCurrentLoginUserEntity();

    EditProfileViewModel getById(Long id);

    void updateProfile(EditProfileServiceModel editProfileServiceModel);
}
