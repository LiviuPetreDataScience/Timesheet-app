package com.findus.services;

import com.findus.models.User;
import com.findus.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(String username, User.Role role) {
        if (username == null || username.trim().isEmpty() || role == null) {
            throw new IllegalArgumentException("Username and role must not be null or empty");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }
        User user = new User(null, username.trim(), role);
        return userRepository.save(user);
    }

    public User updateUser(Long id, String username, User.Role role) {
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (!existingUserOpt.isPresent()) {
            throw new IllegalArgumentException("User not found");
        }
        User existingUser = existingUserOpt.get();
        if (username != null && !username.trim().isEmpty()) {
            // Check for username uniqueness if changed
            Optional<User> userWithSameUsername = userRepository.findByUsername(username.trim());
            if (userWithSameUsername.isPresent() && !userWithSameUsername.get().getId().equals(id)) {
                throw new IllegalArgumentException("Username already exists");
            }
            existingUser.setUsername(username.trim());
        }
        if (role != null) {
            existingUser.setRole(role);
        }
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> login(String username, User.Role role) {
        if (username == null || username.trim().isEmpty() || role == null) {
            return Optional.empty();
        }
        Optional<User> userOpt = userRepository.findByUsername(username.trim());
        if (userOpt.isPresent() && userOpt.get().getRole() == role) {
            return userOpt;
        }
        return Optional.empty();
    }
}