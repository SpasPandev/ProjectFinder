package com.example.projectfinder.repository;

import com.example.projectfinder.model.entity.RoleEntity;
import com.example.projectfinder.model.entity.enums.RoleNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    RoleEntity findByRole(RoleNameEnum role);

    @Query("SELECT r.role FROM RoleEntity AS r WHERE r.id = ?1")
    String findRoleName(Long roleId);


}
