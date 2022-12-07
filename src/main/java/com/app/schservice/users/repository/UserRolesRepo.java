package com.app.schservice.users.repository;

import com.app.schservice.users.model.User;
import com.app.schservice.users.model.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRolesRepo extends JpaRepository<UserRoles,Long> {
    List<UserRoles> findByUser(User user);
}
