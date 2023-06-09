package com.example.projectfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProjectFinderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectFinderApplication.class, args);
    }

}
