package com.maahir.researchlnkapi.model.entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name ="relevant_experiences")
public class RelevantExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name ="start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name ="end_at")
    private LocalDateTime endAt;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    @ToString.Exclude
    private SwipeCard swipeCard;

}
