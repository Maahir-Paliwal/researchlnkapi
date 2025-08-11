package com.maahir.researchlnkapi.model.repositories;

import com.maahir.researchlnkapi.model.entities.RelevantExperience;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RelevantExperienceRepository extends JpaRepository<RelevantExperience, Long> {

    @Query("""
        SELECT re FROM RelevantExperience re 
        WHERE re.swipeCard.id = :cardId
        ORDER BY 
            CASE WHEN re.endAt is NULL THEN 1 ELSE 0 END DESC,
            COALESCE(re.endAt, re.startAt) DESC,
            re.startAt DESC
    """)
    List<RelevantExperience> findOrderedBySwipeCardId(@Param("cardId") Long cardId);

    long countBySwipeCard_Id(Long swipeCardId);

    @EntityGraph(attributePaths = "swipeCard")
    @Query("""
        SELECT re FROM RelevantExperience re
        WHERE re.id =:id
    """)
    Optional<RelevantExperience> findWithCardById(@Param("id") Long id);


    boolean existsByIdAndSwipeCard_Id(Long id, Long swipeCardId);


    int deleteByIdAndSwipeCard_Id( Long id, Long CardId);

}
