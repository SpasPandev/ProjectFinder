package com.example.projectfinder.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @Column(nullable = true, columnDefinition = "LONGTEXT")
    private String profile_picture_url;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<RoleEntity> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<TechnologyEntity> technologies;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "PROJECT_PARTICIPANT",
            joinColumns = @JoinColumn(name = "PARTICIPANT_ID"),
            inverseJoinColumns = @JoinColumn(name = "PROJECT_ID")
    )
    private List<ProjectEntity> listOfProjects = new ArrayList<>();

    public void addProjectForParticipant(ProjectEntity projectEntity)
    {

        listOfProjects.add(projectEntity);
    }

    public void addTechnologiesForUser(TechnologyEntity technologyEntity)
    {

        technologies.add(technologyEntity);
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

    public String getProfile_picture_url() {
        return profile_picture_url;
    }

    public void setProfile_picture_url(String profile_picture_url) {
        this.profile_picture_url = profile_picture_url;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    public Set<TechnologyEntity> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(Set<TechnologyEntity> technologies) {
        this.technologies = technologies;
    }

    public List<ProjectEntity> getListOfProjects() {
        return listOfProjects;
    }

    public void setListOfProjects(List<ProjectEntity> listOfProjects) {
        this.listOfProjects = listOfProjects;
    }

    public UserEntity() {
    }
}
