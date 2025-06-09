package com.findus.controllers;

import com.findus.models.User;
import com.findus.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("roles", Arrays.asList(User.Role.values()));
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("role") User.Role role,
            HttpSession session,
            Model model
    ) {
        return userService.login(username, role)
                .map(user -> {
                    session.setAttribute("loggedUser", user);
                    if (user.getRole() == User.Role.ADMIN) {
                        return "redirect:/user-admin";
                    } else if (user.getRole() == User.Role.PROJECT_MANAGER) {
                        return "redirect:/project-manager/projects";
                    }
                    model.addAttribute("loginError", "Only Admins and Project Managers can access the system at this time.");
                    model.addAttribute("roles", Arrays.asList(User.Role.values()));
                    return "login";
                })
                .orElseGet(() -> {
                    model.addAttribute("loginError", "Invalid username or role.");
                    model.addAttribute("roles", Arrays.asList(User.Role.values()));
                    return "login";
                });
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "logout";
    }
}