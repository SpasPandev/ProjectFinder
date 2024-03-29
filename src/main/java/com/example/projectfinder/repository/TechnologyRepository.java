package com.example.projectfinder.repository;

import com.example.projectfinder.model.entity.TechnologyEntity;
import com.example.projectfinder.model.entity.enums.TechnologyNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TechnologyRepository extends JpaRepository<TechnologyEntity, Long> {

    Optional<TechnologyEntity> findTechnologyEntitiesByTechnologies(TechnologyNameEnum technologyNameEnum);

    List<TechnologyEntity> findByTechnologiesIn(Set<TechnologyNameEnum> technologyName);

    @Query("SELECT t.technologies FROM TechnologyEntity AS t WHERE t.id IN ?1")
    List<String> findTechnologyNameInStringById(List<Long> technologyId);

    List<TechnologyEntity> findTechnologyEntitiesByTechnologiesIn(Set<TechnologyNameEnum> technologyNameEnums);

}
