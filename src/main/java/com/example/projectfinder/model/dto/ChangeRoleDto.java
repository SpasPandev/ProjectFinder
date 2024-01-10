package com.example.projectfinder.model.dto;

import com.example.projectfinder.model.entity.enums.RoleNameEnum;

import javax.validation.constraints.NotNull;

public class ChangeRoleDto {

    @NotNull
    private RoleNameEnum role;

    public ChangeRoleDto() {
    }

    public RoleNameEnum getRole() {
        return role;
    }

    public void setRole(RoleNameEnum role) {
        this.role = role;
    }
}
