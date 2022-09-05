package com.example.projectfinder.repository;

import com.example.projectfinder.model.entity.TechnologyEntity;
import com.example.projectfinder.model.entity.UserEntity;
import com.example.projectfinder.model.entity.enums.TechnologyNameEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TechnologyRepository extends JpaRepository<TechnologyEntity, Long> {

    Optional<TechnologyEntity> findTechnologyEntitiesByTechnologies(TechnologyNameEnum technologyNameEnum );

}