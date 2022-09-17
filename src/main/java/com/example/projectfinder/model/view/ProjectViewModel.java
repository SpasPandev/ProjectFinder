package com.example.projectfinder.model.view;

import com.example.projectfinder.model.entity.ProjectParticipant;
import com.example.projectfinder.model.entity.TechnologyEntity;
import com.example.projectfinder.model.entity.UserEntity;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.Set;

public class ProjectViewModel {

    private Long id;

    private String title;

    private String project_description;

    private Set<TechnologyEntity> technologies;

    private UserEntity author;

    Set<ProjectParticipant> participant;

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
}
