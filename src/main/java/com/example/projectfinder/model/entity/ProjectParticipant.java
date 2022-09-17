package com.example.projectfinder.model.entity;

import com.example.projectfinder.model.embedable.ProjectParticipantKey;

import javax.persistence.*;

@Entity
public class ProjectParticipant {

    @EmbeddedId
    private ProjectParticipantKey id = new ProjectParticipantKey();

    @ManyToOne
    @MapsId("participantId")
    @JoinColumn(name = "participant_id")
    private UserEntity participant;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    private String link;

    public ProjectParticipant() {
    }

    public ProjectParticipantKey getId() {
        return id;
    }

    public void setId(ProjectParticipantKey id) {
        this.id = id;
    }

    public UserEntity getParticipant() {
        return participant;
    }

    public void setParticipant(UserEntity participant) {
        this.participant = participant;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
