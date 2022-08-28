package com.example.projectfinder.model.entity;

import com.example.projectfinder.model.entity.enums.TechnologyNameEnum;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "technologies")
public class TechnologyEntity extends BaseEntity{

    @Enumerated(EnumType.STRING)
    private TechnologyNameEnum technologies;

    public TechnologyNameEnum getTechnologies() {
        return technologies;
    }

    public void setTechnologies(TechnologyNameEnum technologies) {
        this.technologies = technologies;
    }

    public TechnologyEntity() {
    }
}
