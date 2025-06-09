package com.findus.services;

import com.findus.models.Project;
import com.findus.repositories.ProjectRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getProjectsByManager(String projectManagerUsername) {
        if (projectManagerUsername == null || projectManagerUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("Project manager username must not be null or empty");
        }
        return projectRepository.findByProjectManagerUsername(projectManagerUsername.trim());
    }

    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    public Project createProject(String name, String projectManagerUsername, LocalDate startDate, LocalDate endDate, Project.Status status) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Project name must not be null or empty");
        }
        if (projectManagerUsername == null || projectManagerUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("Project manager username must not be null or empty");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("Start date must not be null");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("End date must not be null");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
        if (status == null) {
            status = Project.Status.OPEN;
        }
        Project project = new Project(null, name.trim(), projectManagerUsername.trim(), startDate, endDate, status);
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, String name, LocalDate startDate, LocalDate endDate, Project.Status status, String projectManagerUsername) {
        Optional<Project> existingOpt = projectRepository.findById(id);
        if (!existingOpt.isPresent()) {
            throw new IllegalArgumentException("Project not found");
        }
        Project existing = existingOpt.get();

        if (projectManagerUsername == null || !projectManagerUsername.equalsIgnoreCase(existing.getProjectManagerUsername())) {
            throw new IllegalArgumentException("You are not authorized to update this project");
        }

        if (name != null && !name.trim().isEmpty()) {
            existing.setName(name.trim());
        }
        if (startDate != null) {
            existing.setStartDate(startDate);
        }
        if (endDate != null) {
            if (startDate != null && endDate.isBefore(startDate)) {
                throw new IllegalArgumentException("End date cannot be before start date");
            }
            if (startDate == null && endDate.isBefore(existing.getStartDate())) {
                throw new IllegalArgumentException("End date cannot be before start date");
            }
            existing.setEndDate(endDate);
        }
        if (status != null) {
            existing.setStatus(status);
        }
        return projectRepository.save(existing);
    }
}