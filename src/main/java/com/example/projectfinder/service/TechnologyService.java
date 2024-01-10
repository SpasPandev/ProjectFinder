package com.example.projectfinder.service;

import com.example.projectfinder.model.entity.TechnologyEntity;
import com.example.projectfinder.model.entity.enums.TechnologyNameEnum;
import com.example.projectfinder.repository.TechnologyRepository;
import com.example.projectfinder.web.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TechnologyService {

    private final TechnologyRepository technologyRepository;

    public TechnologyService(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    public TechnologyEntity findTechnologyByName(TechnologyNameEnum technologyNameEnum) {

        return technologyRepository
                .findTechnologyEntitiesByTechnologies(technologyNameEnum)
                .orElseThrow(() -> new ObjectNotFoundException("Technologies were not found!"));
    }
}
