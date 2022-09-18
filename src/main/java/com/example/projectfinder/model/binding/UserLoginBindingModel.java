package com.example.projectfinder.model.binding;

import javax.validation.constraints.NotEmpty;

public class UserLoginBindingModel {

    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

    public UserLoginBindingModel() {
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
}
