package com.maahir.researchlnkapi.model.repositories;

import com.maahir.researchlnkapi.model.entities.ProfileExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfileExperienceRepository extends JpaRepository<ProfileExperience, Long> {

    @Query("""
        select pe from ProfileExperience pe
        WHERE pe.profile.id =: profileId
        ORDER BY
                CASE WHEN pe.endAt IS NULL THEN 1 ELSE 0 END DESC,
                COALESCE(pe.endAt, pe.startAt) DESC, 
                pe.startAt DESC
    """)
    List<ProfileExperience> findOrderedByProfileId(@Param("profileId") Long profileId);

    long countByProfile_Id(Long profileId);

    Optional<ProfileExperience> findByIdAndProfile_Id(Long id, Long profileId);

    long deleteByIdAndProfile_Id(Long id, Long profileId);

    boolean existsByIdAndProfile_Id(Long id, Long profileId);
}
