package com.example.komunikator.repository;

import com.example.komunikator.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo   extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
