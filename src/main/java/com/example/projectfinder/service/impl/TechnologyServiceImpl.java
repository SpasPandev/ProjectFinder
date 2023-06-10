package com.example.projectfinder.service.impl;

import com.example.projectfinder.model.entity.TechnologyEntity;
import com.example.projectfinder.model.entity.enums.TechnologyNameEnum;
import com.example.projectfinder.repository.TechnologyRepository;
import com.example.projectfinder.service.TechnologyService;
import com.example.projectfinder.web.exception.ObjectNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TechnologyServiceImpl implements TechnologyService {

    private final TechnologyRepository technologyRepository;

    public TechnologyServiceImpl(TechnologyRepository technologyRepository) {
        this.technologyRepository = technologyRepository;
    }

    @Override
    public TechnologyEntity findTechnologyByName(TechnologyNameEnum technologyNameEnum) {

        return technologyRepository
                .findTechnologyEntitiesByTechnologies(technologyNameEnum)
                .orElseThrow(() -> new ObjectNotFoundException("Technologies were not found!"));
    }
}
