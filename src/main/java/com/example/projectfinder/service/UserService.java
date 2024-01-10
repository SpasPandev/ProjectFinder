package com.example.projectfinder.service;

import com.example.projectfinder.model.dto.ChangeRoleDto;
import com.example.projectfinder.model.dto.EditProfileDto;
import com.example.projectfinder.model.dto.UserRegisterReqDto;
import com.example.projectfinder.model.dto.UserLoginDto;
import com.example.projectfinder.model.entity.*;
import com.example.projectfinder.model.service.UserServiceModel;
import com.example.projectfinder.model.view.UserViewModel;
import com.example.projectfinder.repository.*;
import com.example.projectfinder.web.exception.ObjectNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final TechnologyRepository technologyRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProjectRepository projectRepository;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, RoleRepository roleRepository, TechnologyRepository technologyRepository, PasswordEncoder passwordEncoder, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.technologyRepository = technologyRepository;
        this.passwordEncoder = passwordEncoder;
        this.projectRepository = projectRepository;
    }

    public void registerUser(UserRegisterReqDto userRegisterReqDto) {

        RoleEntity userRole = this.roleRepository.findByRole(userRegisterReqDto.getRole());
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(userRole);

        List<TechnologyEntity> technologies = technologyRepository.findTechnologyEntitiesByTechnologiesIn(userRegisterReqDto.getTechnology());

        UserEntity userEntity = modelMapper.map(userRegisterReqDto, UserEntity.class);

        userEntity.setPassword(passwordEncoder.encode(userRegisterReqDto.getPassword()));

        userEntity.setRoles(roles);

        userEntity.setTechnologies(technologies);

        userRepository.save(userEntity);
    }

    public Optional<UserLoginDto> findUserByUsername(String username) {

        return userRepository.findByUsername(username)
                .map(userEntity -> modelMapper.map(userEntity, UserLoginDto.class));
    }

    public UserViewModel findUserViewModelByUserId(Long userId) {

        return modelMapper.map(findUserEntityById(userId), UserViewModel.class);

    }

    public boolean isUsernameExists(String username) {

        return userRepository
                .findByUsername(username).isPresent();
    }

    public UserEntity findCurrentLoginUserEntity(Long currentUserId) {

        return findUserEntityById(currentUserId);
    }

    public void updateProfile(EditProfileDto editProfileDto) {

        UserEntity userEntity = findUserEntityById(editProfileDto.getId());

        userEntity.setName(editProfileDto.getName());
        userEntity.setUsername(editProfileDto.getUsername());
        userEntity.setEmail(editProfileDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(editProfileDto.getPassword()));
        userEntity.setDescription(editProfileDto.getDescription());

        List<TechnologyEntity> technologies = technologyRepository.findByTechnologiesIn(editProfileDto.getTechnology());

        userEntity.setTechnologies(technologies);

        this.userRepository.save(userEntity);
    }

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

    public List<String> findUserTechnologyNameInString(Long currentUserId) {

        List<Long> currentUserTechnologyId = userRepository.findTechnologyIdsByUserId(currentUserId);

        return technologyRepository.findTechnologyNameInStringById(currentUserTechnologyId);
    }

    public void adminChangeUserRole(ChangeRoleDto changeRoleDto, Long id) {

        RoleEntity userRole = this.roleRepository.findByRole(changeRoleDto.getRole());
        List<RoleEntity> roles = new ArrayList<>();
        roles.add(userRole);

        UserEntity userEntity = findUserEntityById(id);

        userEntity.setRoles(roles);

        userRepository.save(userEntity);
    }

    public String findUserRoleNameInString(Long currentUserId) {

        Long userRoleId = userRepository.findUserRoleId(currentUserId);
        return roleRepository.findRoleName(userRoleId);
    }

    public boolean isEmailExists(String email) {

        return userRepository.findByEmail(email).isPresent();
    }

    public void deleteUserById(Long userId) {

        List<ProjectEntity> allProjectsForAuthor = projectRepository.findAllByAuthor_Id(userId);

        if (!allProjectsForAuthor.isEmpty()) {
            allProjectsForAuthor.forEach(project ->
                    projectRepository.deleteById(project.getId()));
        } else {
            userRepository.deleteById(userId);
        }

    }

    public void setIsDeletedStatusTrue(Long id) {

        List<ProjectEntity> allProjectsForAuthor = projectRepository.findAllByAuthor_Id(id);

        if (!allProjectsForAuthor.isEmpty()) {
            allProjectsForAuthor.forEach(project -> project.setDeleted(true));
        }

        userRepository.getReferenceById(id).setDeleted(true);
    }

    public List<Long> finsUserTechnologiesIdsByUserId(Long userId) {

        return userRepository.findTechnologyIdsByUserId(userId);
    }

    public com.example.projectfinder.model.dto.EditProfileDto getEditProfileDtoById(Long id) {
        return modelMapper.map(userRepository.findById(id).get(), com.example.projectfinder.model.dto.EditProfileDto.class);
    }

    private UserEntity findUserEntityById(Long id) {

        return userRepository.findById(id).orElseThrow(() ->
                new ObjectNotFoundException("User with id: " + id + " was not Found!"));
    }
}

