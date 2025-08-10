package com.maahir.researchlnkapi.model.repositories;

import com.maahir.researchlnkapi.model.entities.Connection;
import com.maahir.researchlnkapi.model.keys.ConnectionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionRepository extends JpaRepository<Connection, ConnectionId> {

}
