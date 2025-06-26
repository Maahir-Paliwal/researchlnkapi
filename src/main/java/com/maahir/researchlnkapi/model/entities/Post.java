package com.maahir.researchlnkapi.model.entities;

import jakarta.persistence.*;
import lombok.*;
import com.maahir.researchlnkapi.model.enums.ViewType;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name ="postType", nullable = false)
    private String postType;

    @Enumerated(EnumType.STRING)
    @Column(name = "view", nullable = false)
    private ViewType view;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name ="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id")
    @ToString.Exclude
    private Profile profile;

}
