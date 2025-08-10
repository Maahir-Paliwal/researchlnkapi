package com.maahir.researchlnkapi.services;


import com.maahir.researchlnkapi.dtos.connections.ConnectionRequestDto;
import com.maahir.researchlnkapi.dtos.connections.ConnectionStatusDto;
import com.maahir.researchlnkapi.mappers.ConnectionMapper;
import com.maahir.researchlnkapi.model.entities.Connection;
import com.maahir.researchlnkapi.model.entities.User;
import com.maahir.researchlnkapi.model.enums.ConnectionStatus;
import com.maahir.researchlnkapi.model.keys.ConnectionId;
import com.maahir.researchlnkapi.model.repositories.ConnectionRepository;
import com.maahir.researchlnkapi.model.repositories.UserRepository;
import com.maahir.researchlnkapi.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ConnectionService {
    private final ConnectionRepository connectionRepository;
    private final ConnectionMapper connectionMapper;
    private final UserRepository userRepository;

    @Autowired
    public ConnectionService(ConnectionRepository connectionRepository, ConnectionMapper connectionMapper, UserRepository userRepository){
        this.connectionRepository = connectionRepository;
        this.connectionMapper = connectionMapper;
        this.userRepository = userRepository;
    }

    public ConnectionStatusDto request(Object principal, ConnectionRequestDto requestDto){
        Long actorProfileId = extractProfileIdFromPrincipal(principal);
        User targetUser = userRepository.findByEmail(requestDto.getTargetEmail())
                .orElseThrow(() -> new IllegalArgumentException("Target user not found by email"));
        Long targetProfileId = targetUser.getProfile().getId();

        if (Objects.equals(actorProfileId, targetProfileId)){
            throw new IllegalArgumentException("Cannot connect to yourself");
        }

        ConnectionId connectionId = orderConnectionPair(actorProfileId, targetProfileId);
        var existingOpt = connectionRepository.findById(connectionId);

        Connection connection;
        if (existingOpt.isEmpty()) {
            connection = Connection.builder()
                    .connectorId(connectionId.getConnectorId())
                    .connecteeId(connectionId.getConnecteeId())
                    .requesterId(actorProfileId)
                    .connectionStatus(ConnectionStatus.PENDING)
                    .build();
            connectionRepository.save(connection);
        } else {
            connection = existingOpt.get();
            if (connection.getConnectionStatus() == ConnectionStatus.PENDING
            && !Objects.equals(connection.getRequesterId(), actorProfileId)){
                connection.setConnectionStatus(ConnectionStatus.ACCEPTED);
                connectionRepository.save(connection);
            }
        }

        return connectionMapper.toDto(connection);
    }

    public ConnectionStatusDto accept(Long actorProfileId, Long targetProfileId){
        ConnectionId connectionId = orderConnectionPair(actorProfileId, targetProfileId);
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new IllegalArgumentException("No request exists"));

        if (connection.getConnectionStatus() != ConnectionStatus.PENDING){
            throw new IllegalArgumentException("Connection is not pending");
        }

        if (Objects.equals(connection.getRequesterId(), actorProfileId)){
            throw new IllegalArgumentException("Requester cannot accept their own request");
        }

        connection.setConnectionStatus(ConnectionStatus.ACCEPTED);
        connectionRepository.save(connection);

        return connectionMapper.toDto(connection);
    }

    public void reject(Long actorProfileId, Long targetProfileId){
        ConnectionId connectionId = orderConnectionPair(actorProfileId, targetProfileId);
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new IllegalArgumentException("No request exists"));

        connectionRepository.deleteById(connectionId);
    }

    public boolean isConnected(Long actorProfileId, Long targetProfileId){
        if (Objects.equals(actorProfileId, targetProfileId)){
            throw new IllegalArgumentException("Cannot check connection to yourself");
        }

        ConnectionId connectionId = orderConnectionPair(actorProfileId, targetProfileId);
        return connectionRepository.existsByConnectorIdAndConnecteeIdAndConnectionStatus(
                connectionId.getConnectorId(), connectionId.getConnecteeId(), ConnectionStatus.ACCEPTED);
    }

    public List<Connection> listAccepted(Long profileId){
        return connectionRepository.findAllAcceptedFor(profileId);
    }

    public List<Connection> listPending(Long profileId){

        return connectionRepository.findAllPendingFor(profileId);
    }

    private ConnectionId orderConnectionPair(Long a, Long b){
        long min = Math.min(a, b);
        long max = Math.max(a, b);
        return new ConnectionId(min, max);
    }

    private Long extractProfileIdFromPrincipal(Object principal){
        Long userId;
        if (principal instanceof OAuth2User oauthUser){
            Object rawId = oauthUser.getAttribute("id");
            if (rawId instanceof Number n) {
                userId = n.longValue();
            } else {
                userId = Long.valueOf(rawId.toString());
            }
            User user = userRepository.findById(userId).
                    orElseThrow(() -> new RuntimeException("User not found by Id"));
            return user.getProfile().getId();

        } else if (principal instanceof CustomUserDetails customUserDetails) {
            userId = customUserDetails.getUserEntity().getId();
            User user = userRepository.findById(userId).
                    orElseThrow(() -> new RuntimeException("User not found by Id"));
            return user.getProfile().getId();

        } else {
            throw new RuntimeException("Unsupported principal type: " + principal.getClass().getSimpleName());
        }
    }
}
