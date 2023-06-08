package com.example.projectfinder.model.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "project")
public class ProjectEntity extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String project_description;

    @ManyToMany
    private List<TechnologyEntity> technologies;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private UserEntity author;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<ProjectParticipant> participant;

    @Column(name = "is_deleted", columnDefinition = "boolean default false", nullable = false)
    private boolean isDeleted;

    @Column(name = "views_count", nullable = false)
    private int viewsCount;

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

    public List<TechnologyEntity> getTechnologies() {
        return technologies;
    }

    public ProjectEntity setTechnologies(List<TechnologyEntity> technologies) {
        this.technologies = technologies;
        return this;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }

    public List<ProjectParticipant> getParticipant() {
        return participant;
    }

    public ProjectEntity setParticipant(List<ProjectParticipant> participant) {
        this.participant = participant;
        return this;
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

    public ProjectEntity setViewsCount(int viewsCount) {
        this.viewsCount = viewsCount;
        return this;
    }

    public ProjectEntity() {
    }

}
