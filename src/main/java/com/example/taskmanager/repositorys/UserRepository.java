package com.example.taskmanager.repositorys;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.taskmanager.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
}