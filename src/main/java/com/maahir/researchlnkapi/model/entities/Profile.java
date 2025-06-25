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
@Table(name = "profiles")
public class Profile {
    @Id
    @Column(name ="id")
    private Long id;

    @Column(name = "position")
    private String position;

    @Column(name = "description")
    private String description;

    @Column(name ="profile_picture")
    private String profilePicture;

    //Eager b/c we will always show their name, sometimes need orcidID, etc.
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    @MapsId
    private User user;

}
