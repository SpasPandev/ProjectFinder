package com.example.projectfinder.repository;

import com.example.projectfinder.model.embedable.ProjectParticipantKey;
import com.example.projectfinder.model.entity.ProjectEntity;
import com.example.projectfinder.model.entity.ProjectParticipant;
import com.example.projectfinder.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectParticipantRepository extends JpaRepository<ProjectParticipant, ProjectParticipantKey> {

    List<ProjectParticipant> findProjectParticipantByProject(ProjectEntity projectEntity);

    @Query(value = "SELECT * FROM project_participant\n" +
            "WHERE project_id = ?1 AND participant_id = ?2 ", nativeQuery = true)
    ProjectParticipant findCurrentUserAndCurrentProject(ProjectEntity currentProject, UserEntity currentUser);
    ProjectParticipant findAllByProjectAndParticipant(ProjectEntity currentProject, UserEntity currentUser);

    List<ProjectParticipant> findAllByProject_IdAndLinkIsNotNull(Long currentProjectId);

}
