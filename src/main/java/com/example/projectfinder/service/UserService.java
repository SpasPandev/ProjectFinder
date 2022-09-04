package com.example.projectfinder.service;

import com.example.projectfinder.model.service.UserServiceModel;

public interface UserService {
    void registerUser(UserServiceModel userServiceModel);

    UserServiceModel findUserByUsernameAndPassword(String username, String password);

    void loginUser(Long id, String username);

    UserServiceModel findUserById(Long id);

    boolean isNameExists(String username);
}
