package com.example.projectfinder.model.service;

import com.example.projectfinder.model.entity.enums.RoleNameEnum;
import com.example.projectfinder.model.entity.enums.TechnologyNameEnum;

import java.util.Set;

public class UserServiceModel {

    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String description;
    private RoleNameEnum role;
    private Set<TechnologyNameEnum> technology;
    private String link;

    private boolean isDeleted;

    public UserServiceModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
