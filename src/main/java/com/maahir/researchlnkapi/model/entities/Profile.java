package com.maahir.researchlnkapi.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

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

    @Column(name = "public_id", length =36, nullable = false, unique = true, updatable = false)
    private String publicId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name ="profile_picture", nullable = false)
    private String profilePicture;

    //Eager b/c we will always show their name, sometimes need orcidID, etc.
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    @MapsId
    private User user;

    @OneToOne(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private SwipeCard swipeCard;

    public void setSwipeCard(SwipeCard swipeCard) {
        this.swipeCard = swipeCard;
        swipeCard.setProfile(this);
    }

    @PrePersist
    public void ensurePublicId(){
        if ((publicId == null) || (publicId.isBlank())) publicId = UUID.randomUUID().toString();
    }

}
