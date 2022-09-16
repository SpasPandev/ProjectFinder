package com.example.projectfinder.model.binding;

import com.example.projectfinder.model.entity.enums.RoleNameEnum;

import javax.validation.constraints.NotNull;

public class ChangeRoleBindingModel {

    @NotNull
    private RoleNameEnum role;

    public ChangeRoleBindingModel() {
    }

    public RoleNameEnum getRole() {
        return role;
    }

    public void setRole(RoleNameEnum role) {
        this.role = role;
    }
}
