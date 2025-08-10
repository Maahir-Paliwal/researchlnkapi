package com.maahir.researchlnkapi.dtos.connections;

import com.maahir.researchlnkapi.model.enums.ConnectionStatus;
import lombok.Data;

@Data
public class ConnectionStatusDto {
    private Long connectorId;
    private Long connecteeId;
    private Long requesterId;
    private ConnectionStatus status;
    private boolean meIsRequester;
}
