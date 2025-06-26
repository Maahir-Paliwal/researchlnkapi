package com.maahir.researchlnkapi.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
@Table(name ="swipeCards")
public class SwipeCard {
    @Id
    @Column(name ="id")
    private Long id;

    @Column(name ="name", nullable = false)
    private String name;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name ="description", nullable = false)
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    @MapsId
    private Profile profile;
}
