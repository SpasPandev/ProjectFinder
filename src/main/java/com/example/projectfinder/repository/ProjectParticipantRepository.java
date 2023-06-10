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

    @Query("select p from ProjectParticipant p where p.project = ?1 and p.participant.isDeleted IS false ")
    List<ProjectParticipant> findAllUndeletedProjectParticipantsByProject(ProjectEntity projectEntity);

    ProjectParticipant findAllByProjectAndParticipant(ProjectEntity currentProject, UserEntity currentUser);

    @Query("SELECT p FROM ProjectParticipant AS p " +
            "WHERE p.project.id = ?1 AND p.link IS NOT NULL AND p.participant.isDeleted IS false ")
    List<ProjectParticipant> findAllByProjectIdAndLinkIsNotNullAndParticipantIsUndeleted(Long currentProjectId);

}
