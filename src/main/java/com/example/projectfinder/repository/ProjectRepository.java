package com.example.projectfinder.repository;

import com.example.projectfinder.model.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    @Query(value = "SELECT project_entity_id FROM project_technologies\n" +
            "WHERE technologies_id IN ?1 ", nativeQuery = true)
    Set<Long> findListOfProjectsIdsForConcretTehnologies(List<Long> tehnologyIds);

    List<ProjectEntity> findAllByOrderByIdDesc();

    @Query(value = "SELECT project_id FROM project_participant\n" +
            "WHERE participant_id = ?1 ", nativeQuery = true)
    List<Long> listOfAllProjectsIds(Long currentUserId);

    @Query(value = "SELECT technologies_id FROM project_technologies\n" +
            "WHERE project_entity_id = ?1 ", nativeQuery = true)
    List<Long> findTechnologyIdByProjectId(Long projectId);

    @Query(value = "SELECT * FROM project\n" +
            "WHERE author_id = ?1 ", nativeQuery = true)
    List<ProjectEntity> findAllProjectsForAuthor(Long currentUserId);
}
