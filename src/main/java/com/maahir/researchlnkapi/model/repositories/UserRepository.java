package com.maahir.researchlnkapi.model.repositories;

import com.maahir.researchlnkapi.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByOrcidId(String orcidId);
    Optional<User> findByEmail(String email);


}
