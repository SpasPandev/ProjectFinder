package com.example.projectfinder.repository;

import com.example.projectfinder.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findUserByUsernameAndPassword(String username, String password);

    Optional<UserEntity> findByUsername(String username);
}
