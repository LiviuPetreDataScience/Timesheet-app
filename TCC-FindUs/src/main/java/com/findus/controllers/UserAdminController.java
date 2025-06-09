package com.findus.controllers;

import com.findus.models.User;
import com.findus.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user-admin")
public class UserAdminController {

    private final UserService userService;

    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String userAdminPage(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null || loggedUser.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("roles", Arrays.asList(User.Role.values()));
        model.addAttribute("newUser", new User());
        return "user_admin";
    }

    @PostMapping("/create")
    public String createUser(
            @ModelAttribute("newUser") User newUser,
            HttpSession session,
            Model model
    ) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null || loggedUser.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }
        try {
            userService.createUser(newUser.getUsername(), newUser.getRole());
        } catch (IllegalArgumentException ex) {
            List<User> users = userService.getAllUsers();
            model.addAttribute("users", users);
            model.addAttribute("roles", Arrays.asList(User.Role.values()));
            model.addAttribute("newUser", newUser);
            model.addAttribute("error", ex.getMessage());
            return "user_admin";
        }
        return "redirect:/user-admin";
    }

    @PostMapping("/update/{id}")
    public String updateUser(
            @PathVariable("id") Long id,
            @RequestParam("username") String username,
            @RequestParam("role") User.Role role,
            HttpSession session,
            Model model
    ) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null || loggedUser.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }
        try {
            userService.updateUser(id, username, role);
        } catch (IllegalArgumentException ex) {
            List<User> users = userService.getAllUsers();
            model.addAttribute("users", users);
            model.addAttribute("roles", Arrays.asList(User.Role.values()));
            model.addAttribute("newUser", new User());
            model.addAttribute("error", ex.getMessage());
            return "user_admin";
        }
        return "redirect:/user-admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(
            @PathVariable("id") Long id,
            HttpSession session,
            Model model
    ) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null || loggedUser.getRole() != User.Role.ADMIN) {
            return "redirect:/login";
        }
        // Prevent admin from deleting themselves
        if (loggedUser.getId().equals(id)) {
            List<User> users = userService.getAllUsers();
            model.addAttribute("users", users);
            model.addAttribute("roles", Arrays.asList(User.Role.values()));
            model.addAttribute("newUser", new User());
            model.addAttribute("error", "You cannot delete your own admin account while logged in.");
            return "user_admin";
        }
        userService.deleteUser(id);
        return "redirect:/user-admin";
    }
}