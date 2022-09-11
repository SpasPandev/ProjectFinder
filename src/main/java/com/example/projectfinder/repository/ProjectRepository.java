package com.example.projectfinder.repository;

import com.example.projectfinder.model.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    @Query(value = "SELECT * FROM project t1 INNER JOIN project_participant t2 ON t1.id = t2.project_id " +
            "HAVING t2.participant_id = ?1 AND t2.project_id = ?2 ", nativeQuery = true)
    List<ProjectEntity> findUserParticipateInProject(Long userId, Long projectId);

    List<ProjectEntity> findAllByOrderByIdDesc();
}
