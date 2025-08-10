package com.maahir.researchlnkapi.model.entities;

import com.maahir.researchlnkapi.model.enums.ConnectionStatus;
import com.maahir.researchlnkapi.model.keys.ConnectionId;
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
@Table(name = "connections")
@IdClass(ConnectionId.class)
public class Connection {

    @Id
    @Column(name = "connector_id")
    private Long connectorId;

    @Id
    @Column(name = "connectee_id")
    private Long connecteeId;

    @Column(name ="requester_id", nullable = false)
    private Long requesterId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ConnectionStatus connectionStatus;

    @Column(name ="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name ="updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "connector_id", insertable = false, updatable = false)
    private Profile connector;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "connectee_id", insertable = false, updatable = false)
    private Profile connectee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", insertable = false, updatable = false)
    private Profile requester;

    //key pair validation will be handled in the service layer.
}
