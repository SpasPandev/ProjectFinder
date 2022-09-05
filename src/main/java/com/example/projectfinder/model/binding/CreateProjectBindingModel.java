package com.example.projectfinder.model.binding;

import com.example.projectfinder.model.entity.TechnologyEntity;
import com.example.projectfinder.model.entity.enums.TechnologyNameEnum;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

public class CreateProjectBindingModel {

    private String title;
    private String project_description;
    private Set<TechnologyNameEnum> technologies;

    public CreateProjectBindingModel() {
    }

    @NotEmpty
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @NotEmpty
    public String getProject_description() {
        return project_description;
    }

    public void setProject_description(String project_description) {
        this.project_description = project_description;
    }

    @NotEmpty
    public Set<TechnologyNameEnum> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(Set<TechnologyNameEnum> technologies) {
        this.technologies = technologies;
    }
}
