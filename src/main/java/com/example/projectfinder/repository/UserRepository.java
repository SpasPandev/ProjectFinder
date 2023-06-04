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

    @Query(value = "SELECT technologies_id FROM users_technologies\n" +
            "WHERE user_entity_id = ?1 ", nativeQuery = true)
    List<Long> findTechnologyIdsByUserId(Long userId);

    @Query(value = "SELECT roles_id FROM users_roles\n" +
            "WHERE user_entity_id = ?1 ", nativeQuery = true)
    Long findUserRoleId(Long userId);

    Optional<UserEntity> findByEmail(String email);
}
