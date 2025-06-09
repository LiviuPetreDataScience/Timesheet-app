package com.findus.repositories;

import com.findus.models.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class UserRepository {

    private final List<User> users = new ArrayList<>();
    private long nextId = 1L;

    public UserRepository() {
        // Optionally, add a default admin user for initial access
        users.add(new User(nextId++, "admin", User.Role.ADMIN));
    }

    public List<User> findAll() {
        return Collections.unmodifiableList(users);
    }

    public Optional<User> findById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    public Optional<User> findByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(nextId++);
            users.add(user);
        } else {
            deleteById(user.getId());
            users.add(user);
        }
        return user;
    }

    public void deleteById(Long id) {
        users.removeIf(user -> user.getId().equals(id));
    }
}