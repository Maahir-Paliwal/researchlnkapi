package com.maahir.researchlnkapi.model.repositories;


import com.maahir.researchlnkapi.model.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
