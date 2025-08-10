package com.maahir.researchlnkapi.model.repositories;

import com.maahir.researchlnkapi.model.entities.Connection;
import com.maahir.researchlnkapi.model.enums.ConnectionStatus;
import com.maahir.researchlnkapi.model.keys.ConnectionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConnectionRepository extends JpaRepository<Connection, ConnectionId> {
    boolean existsByConnectorIdAndConnecteeIdAndConnectionStatus(Long connectorId, Long connecteeId, ConnectionStatus connectionStatus);

    @Query("""
        SELECT c FROM Connection c
        WHERE (c.connectorId = :id OR c.connecteeId = :id)
            AND c.connectionStatus = com.maahir.researchlnkapi.model.enums.ConnectionStatus.ACCEPTED
        ORDER BY c.updatedAt DESC
    """)
    List<Connection> findAllAcceptedFor(@Param("id") Long id);


    @Query("""
        SELECT c FROM Connection c
        WHERE (c.connectorId = :id OR c.connecteeId = :id)
            AND c.connectionStatus = com.maahir.researchlnkapi.model.enums.ConnectionStatus.PENDING
            AND c.requesterId <> :id
        ORDER BY c.createdAt DESC
    """)
    List<Connection> findAllPendingFor(@Param("id") Long id);

}
