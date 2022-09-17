package com.example.projectfinder.model.embedable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProjectParticipantKey implements Serializable {

    @Column(name = "participant_id")
    private Long participantId;

    @Column(name = "project_id")
    private Long projectId;

    public ProjectParticipantKey() {
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
