package com.example.projectfinder.model.dto;

import com.example.projectfinder.model.entity.enums.RoleNameEnum;
import com.example.projectfinder.model.entity.enums.TechnologyNameEnum;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class UserRegisterReqDto {

    @NotEmpty
    private String name;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String confirmPassword;
    @NotEmpty
    private String email;
    @NotEmpty
    private String description;
    @NotNull
    private RoleNameEnum role;
    @NotNull
    private Set<TechnologyNameEnum> technology;

    public UserRegisterReqDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RoleNameEnum getRole() {
        return role;
    }

    public void setRole(RoleNameEnum role) {
        this.role = role;
    }

    public Set<TechnologyNameEnum> getTechnology() {
        return technology;
    }

    public void setTechnology(Set<TechnologyNameEnum> technology) {
        this.technology = technology;
    }
}
