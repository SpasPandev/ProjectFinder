package com.example.projectfinder.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String description;

    @ManyToMany
    private List<RoleEntity> roles;

    @ManyToMany
    private List<TechnologyEntity> technologies;

    @OneToMany(mappedBy = "participant", cascade = CascadeType.REMOVE)
    private List<ProjectParticipant> participant;

    @Column(name = "is_deleted", columnDefinition = "boolean default false", nullable = false)
    private boolean isDeleted;

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

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public UserEntity setRoles(List<RoleEntity> roles) {
        this.roles = roles;
        return this;
    }

    public List<TechnologyEntity> getTechnologies() {
        return technologies;
    }

    public UserEntity setTechnologies(List<TechnologyEntity> technologies) {
        this.technologies = technologies;
        return this;
    }

    public List<ProjectParticipant> getParticipant() {
        return participant;
    }

    public UserEntity setParticipant(List<ProjectParticipant> participant) {
        this.participant = participant;
        return this;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public UserEntity() {
    }
}
