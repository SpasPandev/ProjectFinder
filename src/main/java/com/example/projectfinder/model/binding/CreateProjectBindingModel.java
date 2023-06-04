package com.example.projectfinder.model.binding;

import com.example.projectfinder.model.entity.enums.TechnologyNameEnum;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

public class CreateProjectBindingModel {

    @NotEmpty
    private String title;
    @NotEmpty
    private String project_description;
    @NotEmpty
    private Set<TechnologyNameEnum> technologies;

    public CreateProjectBindingModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProject_description() {
        return project_description;
    }

    public void setProject_description(String project_description) {
        this.project_description = project_description;
    }

    public Set<TechnologyNameEnum> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(Set<TechnologyNameEnum> technologies) {
        this.technologies = technologies;
    }
}
