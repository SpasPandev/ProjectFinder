package com.example.projectfinder.model.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "project")
public class ProjectEntity extends BaseEntity{

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String project_description;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<TechnologyEntity> technologies;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private UserEntity author;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<ProjectParticipant> participant;

    @Column(name = "is_deleted", columnDefinition = "boolean default false", nullable = false)
    private boolean isDeleted;

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

    public ProjectEntity() {
    }

}
