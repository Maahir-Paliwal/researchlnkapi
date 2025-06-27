package com.maahir.researchlnkapi.model.repositories;

import com.maahir.researchlnkapi.model.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
