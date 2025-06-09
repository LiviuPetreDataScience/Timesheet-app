package com.findus.controllers;

import com.findus.models.Project;
import com.findus.models.User;
import com.findus.services.ProjectService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/project-manager")
public class ProjectManagerController {

    private final ProjectService projectService;

    public ProjectManagerController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/projects")
    public String viewProjects(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null || loggedUser.getRole() != User.Role.PROJECT_MANAGER) {
            return "redirect:/login";
        }
        List<Project> projects = projectService.getProjectsByManager(loggedUser.getUsername());
        model.addAttribute("projects", projects);
        model.addAttribute("statuses", Arrays.asList(Project.Status.values()));
        model.addAttribute("newProject", new Project());
        return "project_manager_projects";
    }

    @PostMapping("/projects/create")
    public String createProject(
            @ModelAttribute("newProject") Project newProject,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("status") Project.Status status,
            HttpSession session,
            Model model
    ) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null || loggedUser.getRole() != User.Role.PROJECT_MANAGER) {
            return "redirect:/login";
        }
        try {
            projectService.createProject(
                    newProject.getName(),
                    loggedUser.getUsername(),
                    startDate,
                    endDate,
                    status
            );
        } catch (IllegalArgumentException ex) {
            List<Project> projects = projectService.getProjectsByManager(loggedUser.getUsername());
            model.addAttribute("projects", projects);
            model.addAttribute("statuses", Arrays.asList(Project.Status.values()));
            model.addAttribute("newProject", newProject);
            model.addAttribute("error", ex.getMessage());
            return "project_manager_projects";
        }
        return "redirect:/project-manager/projects";
    }

    @PostMapping("/projects/update/{id}")
    public String updateProject(
            @PathVariable("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("status") Project.Status status,
            HttpSession session,
            Model model
    ) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null || loggedUser.getRole() != User.Role.PROJECT_MANAGER) {
            return "redirect:/login";
        }
        try {
            projectService.updateProject(
                    id,
                    name,
                    startDate,
                    endDate,
                    status,
                    loggedUser.getUsername()
            );
        } catch (IllegalArgumentException ex) {
            List<Project> projects = projectService.getProjectsByManager(loggedUser.getUsername());
            model.addAttribute("projects", projects);
            model.addAttribute("statuses", Arrays.asList(Project.Status.values()));
            model.addAttribute("newProject", new Project());
            model.addAttribute("error", ex.getMessage());
            return "project_manager_projects";
        }
        return "redirect:/project-manager/projects";
    }
}