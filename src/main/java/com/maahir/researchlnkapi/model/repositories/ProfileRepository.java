package com.maahir.researchlnkapi.model.repositories;

import com.maahir.researchlnkapi.model.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByPublicId(String publicId);
}
