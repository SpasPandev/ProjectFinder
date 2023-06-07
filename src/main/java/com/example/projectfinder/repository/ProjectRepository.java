package com.example.projectfinder.repository;

import com.example.projectfinder.model.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

    @Query("SELECT p.id FROM ProjectEntity AS p JOIN p.technologies AS t WHERE t.id IN ?1")
    Set<Long> findListOfProjectsIdsForConcretTehnologies(List<Long> tehnologyIds);

    @Query("SELECT p FROM ProjectEntity AS p WHERE p.isDeleted = false ORDER BY p.id DESC")
    List<ProjectEntity> findAllByDeletedIsFalseOrderByIdDesc();

    @Query("SELECT p.id FROM ProjectEntity AS p JOIN p.participant AS par WHERE par.participant.id = ?1")
    List<Long> listOfAllProjectsIds(Long currentUserId);

    @Query("SELECT t.id FROM ProjectEntity AS p JOIN p.technologies AS t WHERE p.id = ?1")
    List<Long> findTechnologyIdByProjectId(Long projectId);

    List<ProjectEntity> findAllByAuthor_Id(Long currentUserId);

    @Query("SELECT p from ProjectEntity AS p where p.isDeleted = true ORDER BY p.id DESC")
    List<ProjectEntity> findAllDeletedProjects();

}
