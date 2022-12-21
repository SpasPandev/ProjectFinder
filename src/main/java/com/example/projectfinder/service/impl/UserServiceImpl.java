package com.example.projectfinder.service.impl;

import com.example.projectfinder.model.entity.*;
import com.example.projectfinder.model.service.EditProfileServiceModel;
import com.example.projectfinder.model.service.UserServiceModel;
import com.example.projectfinder.model.view.EditProfileViewModel;
import com.example.projectfinder.model.view.UserViewModel;
import com.example.projectfinder.repository.*;
import com.example.projectfinder.service.UserService;
import com.example.projectfinder.util.CurrentUser;
import com.example.projectfinder.web.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CurrentUser currentUser;
    private final RoleRepository roleRepository;
    private final TechnologyRepository technologyRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, CurrentUser currentUser, RoleRepository roleRepository, TechnologyRepository technologyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.currentUser = currentUser;
        this.roleRepository = roleRepository;
        this.technologyRepository = technologyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(UserServiceModel userServiceModel) {

        RoleEntity userRole = this.roleRepository.findByRole(userServiceModel.getRole());
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(userRole);

        TechnologyEntity techno = technologyRepository.findByTechnologies(userServiceModel.getTechnology());
        Set<TechnologyEntity> technologies = new HashSet<>();
        technologies.add(techno);

        UserEntity userEntity = modelMapper.map(userServiceModel, UserEntity.class);

        userEntity.setPassword(passwordEncoder.encode(userServiceModel.getPassword()));

        userEntity.setRoles(roles);

        userEntity.setTechnologies(technologies);

        userRepository.save(userEntity);
    }

    @Override
    public UserServiceModel findUserByUsername(String username) {

        return userRepository
                .findByUsername(username)
                .map(userEntity -> modelMapper.map(userEntity, UserServiceModel.class))
                .orElse(null);
    }

    @Override
    public void loginUser(Long id, String username) {

        currentUser.setId(id);
        currentUser.setUsername(username);
        currentUser.setEmail(userRepository.findByUsername(username).get().getEmail());
        currentUser.setRoleName(findUserRoleNameInString(id));
    }

    @Override
    public void logoutUser() {

        currentUser.setId(null);
        currentUser.setUsername(null);
        currentUser.setEmail(null);
        currentUser.setRoleName(null);
    }

    @Override
    public UserServiceModel findUserById(Long id) {

        return userRepository
                .findById(id)
                .map(userEntity -> modelMapper.map(userEntity, UserServiceModel.class))
                .orElse(null);

    }

    @Override
    public boolean isUsernameExists(String username) {

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

        TechnologyEntity techno = technologyRepository.findByTechnologies(editProfileServiceModel.getTechnology());
        Set<TechnologyEntity> technologies = new HashSet<>();
        technologies.add(techno);

        userEntity.setTechnologies(technologies);

        this.userRepository.save(userEntity);
    }

    @Override
    public List<UserViewModel> findAllUsers() {

        List<UserViewModel> userViewModelList = userRepository.findAll().stream()
                .map(userEntity -> {UserViewModel userViewModel = modelMapper
                        .map(userEntity, UserViewModel.class);

                return userViewModel;
                })
                .collect(Collectors.toList());

        List<Long> listOfIds = new ArrayList<>();

        for (int i = 0; i < userViewModelList.size(); i++) {
            listOfIds.add(userRepository.findUserRoleId(userViewModelList.get(i).getId()));
            userViewModelList.get(i).setRole(roleRepository.findRoleName(listOfIds.get(i)));
        }

        return userViewModelList;
    }

    @Override
    public String findUserTechnologyNameInString(Long currentUserId) {

        Long currentUserTechnologyId = userRepository.findTechnologyIdByUserId(currentUserId);

        String currentUserTechnologyName = technologyRepository.findTechnologyNameInStringById(currentUserTechnologyId);

        return currentUserTechnologyName;
    }

    @Override
    public void adminChangeUserRole(UserServiceModel userServiceModel, Long id) {

        RoleEntity userRole = this.roleRepository.findByRole(userServiceModel.getRole());
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(userRole);

        UserEntity userEntity = userRepository.findById(id).get();

        userEntity.setRoles(roles);

        userRepository.save(userEntity);
    }

    @Override
    public boolean isAdmin(CurrentUser currentUser) {

        Long currentUserRoleId = userRepository.findUserRoleId(currentUser.getId());
        String currentUserRoleNameInString = roleRepository.findRoleName(currentUserRoleId);

        if (currentUserRoleNameInString.equals("ADMIN"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public String findUserRoleNameInString(Long currentUserId)
    {
        Long userRoleId = userRepository.findUserRoleId(currentUserId);
        String userRoleNameInString = roleRepository.findRoleName(userRoleId);

        return userRoleNameInString;
    }

    @Override
    public boolean isEmailExists(String email) {

        return userRepository.findByEmail(email).isPresent();
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
