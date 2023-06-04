package com.example.projectfinder.model.entity;

import com.example.projectfinder.model.entity.enums.RoleNameEnum;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class RoleEntity extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private RoleNameEnum role;

    public RoleNameEnum getRole() {
        return role;
    }

    public void setRole(RoleNameEnum role) {
        this.role = role;
    }

    public RoleEntity() {
    }
}
