package com.example.projectfinder.service.impl;

import com.example.projectfinder.model.binding.UserRegisterBindingModel;
import com.example.projectfinder.model.dto.EditProfileDto;
import com.example.projectfinder.model.entity.*;
import com.example.projectfinder.model.service.EditProfileServiceModel;
import com.example.projectfinder.model.service.UserServiceModel;
import com.example.projectfinder.model.view.EditProfileViewModel;
import com.example.projectfinder.model.view.UserViewModel;
import com.example.projectfinder.repository.*;
import com.example.projectfinder.service.UserService;
import com.example.projectfinder.web.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final TechnologyRepository technologyRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProjectRepository projectRepository;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, RoleRepository roleRepository, TechnologyRepository technologyRepository, PasswordEncoder passwordEncoder, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.technologyRepository = technologyRepository;
        this.passwordEncoder = passwordEncoder;
        this.projectRepository = projectRepository;
    }

    @Override
    public void registerUser(UserRegisterBindingModel userRegisterBindingModel) {

        RoleEntity userRole = this.roleRepository.findByRole(userRegisterBindingModel.getRole());
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(userRole);

        List<TechnologyEntity> technologies = technologyRepository.findTechnologyEntitiesByTechnologiesIn(userRegisterBindingModel.getTechnology());

        UserEntity userEntity = modelMapper.map(userRegisterBindingModel, UserEntity.class);

        userEntity.setPassword(passwordEncoder.encode(userRegisterBindingModel.getPassword()));

        userEntity.setRoles(roles);

        userEntity.setTechnologies(technologies);

        userRepository.save(userEntity);
    }

    @Override
    public Optional<UserServiceModel> findUserByUsername(String username) {

        return userRepository
                .findByUsername(username)
                .map(userEntity -> modelMapper.map(userEntity, UserServiceModel.class));
    }

    @Override
    public UserServiceModel findUserById(Long id) {

        UserEntity userEntity = findUserEntityById(id);

        return modelMapper.map(userEntity, UserServiceModel.class);

    }

    @Override
    public boolean isUsernameExists(String username) {

        return userRepository
                .findByUsername(username).isPresent();
    }


    @Override
    public UserEntity findCurrentLoginUserEntity(Long currentUserId) {

        return findUserEntityById(currentUserId);
    }

    @Override
    public void updateProfile(EditProfileServiceModel editProfileServiceModel) {

        UserEntity userEntity = findUserEntityById(editProfileServiceModel.getId());

        userEntity.setName(editProfileServiceModel.getName());
        userEntity.setUsername(editProfileServiceModel.getUsername());
        userEntity.setEmail(editProfileServiceModel.getEmail());
        userEntity.setPassword(editProfileServiceModel.getPassword());
        userEntity.setDescription(editProfileServiceModel.getDescription());

        List<TechnologyEntity> technologies = technologyRepository.findByTechnologiesIn(editProfileServiceModel.getTechnology());

        userEntity.setTechnologies(technologies);

        this.userRepository.save(userEntity);
    }

    @Override
    public List<UserViewModel> findAllUsers() {

        return userRepository.findAllUserWithFetchedRoles()
                .stream()
                .map(userEntity -> {
                    UserViewModel userViewModel = modelMapper.map(userEntity, UserViewModel.class);
                    userViewModel.setRole(userEntity
                            .getRoles()
                            .stream()
                            .findFirst()
                            .orElseThrow()
                            .getRole()
                            .name());
                    return userViewModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findUserTechnologyNameInString(Long currentUserId) {

        List<Long> currentUserTechnologyId = userRepository.findTechnologyIdsByUserId(currentUserId);

        return technologyRepository.findTechnologyNameInStringById(currentUserTechnologyId);
    }

    @Override
    public void adminChangeUserRole(UserServiceModel userServiceModel, Long id) {

        RoleEntity userRole = this.roleRepository.findByRole(userServiceModel.getRole());
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(userRole);

        UserEntity userEntity = findUserEntityById(id);

        userEntity.setRoles(roles);

        userRepository.save(userEntity);
    }

    @Override
    public String findUserRoleNameInString(Long currentUserId) {

        Long userRoleId = userRepository.findUserRoleId(currentUserId);
        return roleRepository.findRoleName(userRoleId);
    }

    @Override
    public boolean isEmailExists(String email) {

        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public void deleteUserById(Long userId) {

        List<ProjectEntity> allProjectsForAuthor = projectRepository.findAllByAuthor_Id(userId);

        if (!allProjectsForAuthor.isEmpty()) {
            allProjectsForAuthor.forEach(project ->
                    projectRepository.deleteById(project.getId()));
        } else {
            userRepository.deleteById(userId);
        }

    }

    @Override
    public void setIsDeletedStatusTrue(Long id) {

        List<ProjectEntity> allProjectsForAuthor = projectRepository.findAllByAuthor_Id(id);

        if (!allProjectsForAuthor.isEmpty()) {
            allProjectsForAuthor.forEach(project -> project.setDeleted(true));
        }

        userRepository.getReferenceById(id).setDeleted(true);
    }

    @Override
    public List<Long> finsUserTechnologiesIdsByUserId(Long userId) {

        return userRepository.findTechnologyIdsByUserId(userId);
    }

    @Override
    public EditProfileDto getEditProfileDtoById(Long id) {
        return modelMapper.map(userRepository.findById(id).get(), EditProfileDto.class);
    }

    private UserEntity findUserEntityById(Long id) {

        return userRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("User with id: " + id + " was not Found!"));
    }
}

