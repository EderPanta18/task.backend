package com.example.taskmanager.services;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.taskmanager.model.User;
import com.example.taskmanager.repositorys.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return user == null ? null : userRepository.save(user);
    }

    public User updateUser(UUID id, User userDetails) {
        User user = getUserdById(id);
        if (user != null && userDetails != null) {
            user.setUsername(userDetails.getUsername());
            user.setPassword(userDetails.getPassword());
            return userRepository.save(user);
        }
        return null;
    }

    public void deleteUser(UUID id) {
        User user = getUserdById(id);
        if (user == null) {
            return;
        }
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    public User getUserdById(UUID id) {
        return userRepository.findById(id).orElse(null);
    }
}
