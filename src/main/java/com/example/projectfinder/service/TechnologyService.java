package com.example.projectfinder.service;

import com.example.projectfinder.model.entity.TechnologyEntity;
import com.example.projectfinder.model.entity.enums.TechnologyNameEnum;

public interface TechnologyService {

    TechnologyEntity findTechnologyByName(TechnologyNameEnum technologyNameEnum);
}
