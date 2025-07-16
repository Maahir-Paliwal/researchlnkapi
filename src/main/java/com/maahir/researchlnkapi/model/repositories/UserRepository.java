package com.maahir.researchlnkapi.model.repositories;

import com.maahir.researchlnkapi.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
