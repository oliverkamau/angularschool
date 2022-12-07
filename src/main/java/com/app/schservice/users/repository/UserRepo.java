package com.app.schservice.users.repository;

import com.app.schservice.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
    User findByUsername(String username);

    User findByUserId(Long userId);

    User findByUsernameAndActiveTrue(String username);
}
