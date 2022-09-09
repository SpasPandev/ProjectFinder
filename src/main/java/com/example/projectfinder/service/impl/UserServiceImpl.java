package com.example.projectfinder.service.impl;

import com.example.projectfinder.model.entity.UserEntity;
import com.example.projectfinder.model.service.EditProfileServiceModel;
import com.example.projectfinder.model.service.UserServiceModel;
import com.example.projectfinder.model.view.EditProfileViewModel;
import com.example.projectfinder.model.view.ProjectViewModel;
import com.example.projectfinder.model.view.UserViewModel;
import com.example.projectfinder.repository.UserRepository;
import com.example.projectfinder.service.UserService;
import com.example.projectfinder.util.CurrentUser;
import com.example.projectfinder.web.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CurrentUser currentUser;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, CurrentUser currentUser) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.currentUser = currentUser;
    }

    @Override
    public void registerUser(UserServiceModel userServiceModel) {

        UserEntity userEntity = modelMapper.map(userServiceModel, UserEntity.class);

        userRepository.save(userEntity);
    }

    @Override
    public UserServiceModel findUserByUsernameAndPassword(String username, String password) {

        return userRepository
                .findUserByUsernameAndPassword(username, password)
                .map(userEntity -> modelMapper.map(userEntity, UserServiceModel.class))
                .orElse(null);
    }

    @Override
    public void loginUser(Long id, String username) {

            currentUser.setId(id);
            currentUser.setUsername(username);
    }

    @Override
    public UserServiceModel findUserById(Long id) {

        return userRepository
                .findById(id)
                .map(userEntity -> modelMapper.map(userEntity, UserServiceModel.class))
                .orElse(null);

    }

    @Override
    public boolean isNameExists(String username) {

        return userRepository
                .findByUsername(username).isPresent();
    }

    @Override
    public UserEntity findCurrentLoginUserEntity() {

        return userRepository
                .findById(currentUser.getId())
                .orElse(null);
    }

    @Override
    public EditProfileViewModel getById(Long id) {

        return this.userRepository.findById(id)
                .map(this::mapProfileDetailsView)
                .orElse(null);
    }

    @Override
    public void updateProfile(EditProfileServiceModel editProfileServiceModel) {

        UserEntity userEntity = this.userRepository.findById(editProfileServiceModel.getId())
                .orElseThrow(() -> new ObjectNotFoundException("User with id " + editProfileServiceModel.getId() +
                        " not found !"));

        userEntity.setName(editProfileServiceModel.getName());
        userEntity.setUsername(editProfileServiceModel.getUsername());
        userEntity.setEmail(editProfileServiceModel.getEmail());
        userEntity.setPassword(editProfileServiceModel.getPassword());
        userEntity.setDescription(editProfileServiceModel.getDescription());

        this.userRepository.save(userEntity);
    }

    @Override
    public List<UserViewModel> findAllUsers() {

        return userRepository.findAll().stream()
                .map(userEntity -> {UserViewModel userViewModel = modelMapper
                        .map(userEntity, UserViewModel.class);

                return userViewModel;
                })
                .collect(Collectors.toList());
    }

    private EditProfileViewModel mapProfileDetailsView(UserEntity userEntity) {
        EditProfileViewModel editProfileViewModel = modelMapper.map(userEntity, EditProfileViewModel.class);

        editProfileViewModel.setName(userEntity.getName());
        editProfileViewModel.setUsername(userEntity.getUsername());
        editProfileViewModel.setEmail(userEntity.getEmail());
        editProfileViewModel.setPassword(userEntity.getPassword());
        editProfileViewModel.setDescription(userEntity.getDescription());


        return editProfileViewModel;
    }
}
