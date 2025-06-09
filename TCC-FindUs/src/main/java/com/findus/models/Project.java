package com.findus.models;

import java.time.LocalDate;
import java.util.Objects;

public class Project {

    public enum Status {
        OPEN,
        IN_PROGRESS,
        CLOSED
    }

    private Long id;
    private String name;
    private String projectManagerUsername;
    private LocalDate startDate;
    private LocalDate endDate;
    private Status status;

    public Project() {
    }

    public Project(Long id, String name, String projectManagerUsername, LocalDate startDate, LocalDate endDate, Status status) {
        this.id = id;
        this.name = name;
        this.projectManagerUsername = projectManagerUsername;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectManagerUsername() {
        return projectManagerUsername;
    }

    public void setProjectManagerUsername(String projectManagerUsername) {
        this.projectManagerUsername = projectManagerUsername;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        Project project = (Project) o;
        return Objects.equals(id, project.id) &&
                Objects.equals(name, project.name) &&
                Objects.equals(projectManagerUsername, project.projectManagerUsername) &&
                Objects.equals(startDate, project.startDate) &&
                Objects.equals(endDate, project.endDate) &&
                status == project.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, projectManagerUsername, startDate, endDate, status);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", projectManagerUsername='" + projectManagerUsername + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                '}';
    }
}