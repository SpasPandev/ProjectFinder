package com.example.projectfinder.repository;

import com.example.projectfinder.model.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    @Query("SELECT p FROM ProjectEntity AS p JOIN FETCH p.technologies AS t " +
            "WHERE t.id in ?1 AND p.isDeleted IS false")
    List<ProjectEntity> findAllUndeletedProjectsByTechnologiesIn(List<Long> technologiesIds);

    @Query("SELECT p FROM ProjectEntity AS p WHERE p.isDeleted = false ORDER BY p.id DESC")
    List<ProjectEntity> findAllByDeletedIsFalseOrderByIdDesc();

    @Query("SELECT p FROM ProjectEntity AS p JOIN p.participant AS par " +
            "WHERE par.participant.id = ?1 AND p.isDeleted IS false")
    List<ProjectEntity> findAllUndeletedProjectsByCurrentUserId(Long currentUserId);

    @Query("SELECT t.id FROM ProjectEntity AS p JOIN p.technologies AS t WHERE p.id = ?1")
    List<Long> findTechnologyIdByProjectId(Long projectId);

    List<ProjectEntity> findAllByAuthor_Id(Long currentUserId);

    @Query("SELECT p from ProjectEntity AS p where p.isDeleted = true ORDER BY p.id DESC")
    List<ProjectEntity> findAllDeletedProjects();

    @Query("SELECT COUNT(p) > 0 FROM ProjectEntity AS p WHERE p.author.id = ?1 AND p.id = ?2")
    boolean isCurrentUserAuthorForThisProject(Long currentUserId, Long currentProjectId);

}
