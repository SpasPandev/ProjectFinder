package com.example.projectfinder.service.scheduler;

import com.example.projectfinder.model.entity.ProjectEntity;
import com.example.projectfinder.repository.ProjectRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Scheduler {

    private final ProjectRepository projectRepository;

    public Scheduler(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Scheduled(cron = "0 0 0 ? * MON")
    public void resetViewsCount() {

        List<ProjectEntity> allProjects = projectRepository.findAll();

        allProjects.forEach(projectEntity -> {
            projectEntity.setViewsCount(0);
            projectRepository.save(projectEntity);
        });

    }
}
