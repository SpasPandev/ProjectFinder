package com.example.projectfinder.model.view;

import com.example.projectfinder.model.entity.ProjectParticipant;
import com.example.projectfinder.model.entity.TechnologyEntity;
import com.example.projectfinder.model.entity.UserEntity;

import java.util.Set;

public class ProjectViewModel {

    private Long id;

    private String title;

    private String project_description;

    private Set<TechnologyEntity> technologies;

    private UserEntity author;

    private Set<ProjectParticipant> participant;

    private boolean isDeleted;

    private int viewsCount;

    public ProjectViewModel() {
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

    public Set<TechnologyEntity> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(Set<TechnologyEntity> technologies) {
        this.technologies = technologies;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public Set<ProjectParticipant> getParticipant() {
        return participant;
    }

    public void setParticipant(Set<ProjectParticipant> participant) {
        this.participant = participant;
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

    public ProjectViewModel setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
        return this;
    }
}
