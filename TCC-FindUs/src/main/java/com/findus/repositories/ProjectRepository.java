package com.findus.repositories;

import com.findus.models.Project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProjectRepository {

    private final List<Project> projects = new ArrayList<>();
    private long nextId = 1L;

    public List<Project> findAll() {
        return Collections.unmodifiableList(projects);
    }

    public List<Project> findByProjectManagerUsername(String username) {
        if (username == null) {
            return Collections.emptyList();
        }
        return projects.stream()
                .filter(p -> username.equalsIgnoreCase(p.getProjectManagerUsername()))
                .collect(Collectors.toList());
    }

    public Optional<Project> findById(Long id) {
        return projects.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
    }

    public Project save(Project project) {
        if (project.getId() == null) {
            project.setId(nextId++);
            projects.add(project);
        } else {
            // Update existing project
            deleteById(project.getId());
            projects.add(project);
        }
        return project;
    }

    public void deleteById(Long id) {
        projects.removeIf(p -> p.getId().equals(id));
    }
}