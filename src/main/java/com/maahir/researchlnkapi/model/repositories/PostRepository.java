package com.maahir.researchlnkapi.model.repositories;


import com.maahir.researchlnkapi.model.entities.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
        SELECT p FROM Post p
        WHERE p.profile.id = :profileId
        ORDER BY p.createdAt DESC, p.id DESC
    """)
    List<Post> findOrderedByProfileId(@Param("profileId") Long profileId);

    @EntityGraph(attributePaths = "profile")
    @Query("""
        SELECT p FROM Post p 
        WHERE p.id = :id
    """)
    Optional<Post> findWithProfileById(@Param("id") Long id);

    int deleteByIdAndProfile_Id(Long id, Long profileId);

    boolean existsByIdAndProfile_Id(Long id, Long profileId);
}
