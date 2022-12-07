package com.app.schservice.users.repository;

import com.app.schservice.users.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepo extends JpaRepository<Roles, Long> {

    Roles findFirstByRoleName(String admin);

    Roles findByRoleCode(Long roleCode);
}
