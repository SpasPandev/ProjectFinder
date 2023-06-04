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
        Set<RoleEntity> roles = new HashSet<>();
        roles.add(userRole);

        Set<TechnologyEntity> technologies = technologyRepository.findTechnologyEntitiesByTechnologiesIn(userServiceModel.getTechnology());

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
                .orElse(null);

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

        Set<TechnologyEntity> technologies = technologyRepository.findByTechnologiesIn(editProfileServiceModel.getTechnology());

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
    public List<String> findUserTechnologyNameInString(Long currentUserId) {

        List<Long> currentUserTechnologyId = userRepository.findTechnologyIdsByUserId(currentUserId);

        List<String> currentUserTechnologyNames = technologyRepository.findTechnologyNameInStringById(currentUserTechnologyId);

        return currentUserTechnologyNames;
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

    @Override
    public void deleteUserById(Long userId) {

        List<ProjectEntity> allProjectsForAuthor = projectRepository.findAllProjectsForAuthor(userId);

        if (!allProjectsForAuthor.isEmpty())
        {
            allProjectsForAuthor.forEach(poject ->
                    projectRepository.deleteById(poject.getId()));
        }
        else {
            userRepository.deleteById(userId);
        }

    }

    @Override
    public void setIsDeleatedStatusTrue(Long id) {

        List<ProjectEntity> allProjectsForAuthor = projectRepository.findAllProjectsForAuthor(id);

        if (!allProjectsForAuthor.isEmpty())
        {
            allProjectsForAuthor.forEach(project -> {
                project.setDeleted(true);
            });

            userRepository.findById(id).get().setDeleted(true);
        }
        else
        {
            userRepository.findById(id).get().setDeleted(true);
        }
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
