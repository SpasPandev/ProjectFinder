package com.example.projectfinder.service.impl;

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
    public void registerUser(UserServiceModel userServiceModel) {

        RoleEntity userRole = this.roleRepository.findByRole(userServiceModel.getRole());
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(userRole);

        List<TechnologyEntity> technologies = technologyRepository.findTechnologyEntitiesByTechnologiesIn(userServiceModel.getTechnology());

        UserEntity userEntity = modelMapper.map(userServiceModel, UserEntity.class);

        userEntity.setPassword(passwordEncoder.encode(userServiceModel.getPassword()));

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

        return userRepository
                .findById(id)
                .map(userEntity -> modelMapper.map(userEntity, UserServiceModel.class))
                .orElseThrow();

    }

    @Override
    public boolean isUsernameExists(String username) {

        return userRepository
                .findByUsername(username).isPresent();
    }


    @Override
    public UserEntity findCurrentLoginUserEntity(Long currentUserId) {

        return userRepository
                .findById(currentUserId)
                .orElseThrow(() -> new ObjectNotFoundException("User was not Found!"));
    }

    @Override
    public EditProfileViewModel getById(Long id) {

        return this.userRepository.findById(id)
                .map(this::mapProfileDetailsView)
                .orElseThrow();
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

        UserEntity userEntity = userRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("User was not Found!"));

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
