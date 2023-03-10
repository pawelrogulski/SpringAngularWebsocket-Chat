package com.example.komunikator.repository;

import com.example.komunikator.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    @Query("SELECT u FROM User u WHERE u.username=?1")
    Optional<User> findByUsername(String username);
}
