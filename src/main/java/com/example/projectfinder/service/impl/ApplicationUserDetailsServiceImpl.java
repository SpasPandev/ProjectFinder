package com.example.projectfinder.service.impl;

import com.example.projectfinder.model.entity.UserEntity;
import com.example.projectfinder.repository.UserRepository;
import com.example.projectfinder.web.exception.ObjectNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationUserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public ApplicationUserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(
                () -> new ObjectNotFoundException("User with username: " + username + " not found!"));


        return mapToUserDetails(userEntity);
    }

    private UserDetails mapToUserDetails(UserEntity userEntity) {

        Set<SimpleGrantedAuthority> grantedAuthorities = userEntity
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().name()))
                .collect(Collectors.toUnmodifiableSet());

        return new ApplicationUser(
                userEntity.getUsername(),
                userEntity.getPassword(),
                !userEntity.isDeleted(),
                true,
                true,
                true,
                grantedAuthorities
        )
                .setId(userEntity.getId())
                .setEmail(userEntity.getEmail())
                .setDeleted(userEntity.isDeleted());
    }
}
