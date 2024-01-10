package com.example.projectfinder.model.dto;

import com.example.projectfinder.model.entity.UserEntity;
import com.example.projectfinder.model.entity.enums.TechnologyNameEnum;

import java.util.List;
import java.util.Set;

public class ProjectDto {

    private Long id;

    private String title;

    private String project_description;

    private Set<TechnologyNameEnum> technologies;

    private UserEntity author;

    private List<UserEntity> participants;

    private boolean isDeleted;

    private int viewsCount;

    public ProjectDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProject_description() {
        return project_description;
    }

    public void setProject_description(String project_description) {
        this.project_description = project_description;
    }

    public Set<TechnologyNameEnum> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(Set<TechnologyNameEnum> technologies) {
        this.technologies = technologies;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public List<UserEntity> getParticipants() {
        return participants;
    }

    public void setParticipants(List<UserEntity> participants) {
        this.participants = participants;
    }

    public void addParticipant(UserEntity userEntity) {
        participants.add(userEntity);
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int getViewsCount() {
        return viewsCount;
    }

    public ProjectDto setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
        return this;
    }
}
