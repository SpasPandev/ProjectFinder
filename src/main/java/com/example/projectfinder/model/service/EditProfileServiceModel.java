package com.example.projectfinder.model.service;

import com.example.projectfinder.model.entity.enums.TechnologyNameEnum;

import java.util.Set;

public class EditProfileServiceModel {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String profile_picture_url;
    private String description;
    private Set<TechnologyNameEnum> technology;

    public EditProfileServiceModel() {
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

    public String getProfile_picture_url() {
        return profile_picture_url;
    }

    public void setProfile_picture_url(String profile_picture_url) {
        this.profile_picture_url = profile_picture_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<TechnologyNameEnum> getTechnology() {
        return technology;
    }

    public void setTechnology(Set<TechnologyNameEnum> technology) {
        this.technology = technology;
    }
}
