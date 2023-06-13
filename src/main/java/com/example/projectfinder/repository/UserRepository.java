package com.example.projectfinder.repository;

import com.example.projectfinder.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    @Query("SELECT t.id FROM UserEntity AS u JOIN u.technologies AS t WHERE u.id = ?1")
    List<Long> findTechnologyIdsByUserId(Long userId);

    @Query("SELECT r.id FROM UserEntity AS u JOIN u.roles AS r WHERE u.id = ?1")
    Long findUserRoleId(Long userId);

    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u FROM UserEntity AS u JOIN FETCH u.roles")
    List<UserEntity> findAllUserWithFetchedRoles();

    @Query("SELECT u FROM UserEntity AS u JOIN FETCH u.technologies WHERE u IN ?1")
    UserEntity fetchUserWithFetchedTechnologiesByUserId(UserEntity userEntity);

    @Query("SELECT u FROM UserEntity AS u JOIN FETCH u.roles WHERE u IN ?1")
    UserEntity fetchUserWithFetchedRolesByUserId(UserEntity userEntity);
}
