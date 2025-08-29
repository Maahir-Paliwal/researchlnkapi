package com.maahir.researchlnkapi.services;


import com.maahir.researchlnkapi.dtos.connections.*;
import com.maahir.researchlnkapi.mappers.ConnectionMapper;
import com.maahir.researchlnkapi.model.entities.Connection;
import com.maahir.researchlnkapi.model.entities.Profile;
import com.maahir.researchlnkapi.model.entities.User;
import com.maahir.researchlnkapi.model.enums.ConnectionStatus;
import com.maahir.researchlnkapi.model.keys.ConnectionId;
import com.maahir.researchlnkapi.model.repositories.ConnectionRepository;
import com.maahir.researchlnkapi.model.repositories.ProfileRepository;
import com.maahir.researchlnkapi.model.repositories.UserRepository;
import com.maahir.researchlnkapi.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ConnectionService {
    private final ConnectionRepository connectionRepository;
    private final ConnectionMapper connectionMapper;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public ConnectionService(ConnectionRepository connectionRepository,
                             ConnectionMapper connectionMapper,
                             UserRepository userRepository,
                             ProfileRepository profileRepository){
        this.connectionRepository = connectionRepository;
        this.connectionMapper = connectionMapper;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    public ConnectionStatusDto request(Object principal, String publicId){
        Long actorProfileId = extractProfileIdFromPrincipal(principal);
        Profile profile = profileRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("User now found by publicId: " + publicId));
        Long targetProfileId = profile.getId();

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
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .connectionStatus(ConnectionStatus.PENDING)
                    .build();
            connectionRepository.save(connection);
        } else {
            connection = existingOpt.get();
            if (connection.getConnectionStatus() == ConnectionStatus.PENDING
            && !Objects.equals(connection.getRequesterId(), actorProfileId)){
                connection.setConnectionStatus(ConnectionStatus.ACCEPTED);
                connection.setUpdatedAt(LocalDateTime.now());
                connectionRepository.save(connection);
            }
        }
        ConnectionStatusDto dto = connectionMapper.toDto(connection);

        Profile requester = profileRepository.findById(actorProfileId)
                .orElseThrow(() -> new IllegalStateException("Requester profile missing"));

        dto.setRequesterPublicId(requester.getPublicId());
        dto.setRequesterName(requester.getName());
        dto.setRequesterPosition(requester.getPosition());
        dto.setRequesterProfilePicture(requester.getProfilePicture());
        return dto;
    }


    public ConnectionStatusDto accept(Object principal, String publicId){
        Long actorProfileId = extractProfileIdFromPrincipal(principal);
        Profile profile = profileRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("User now found by publicId: " + publicId));
        Long targetProfileId = profile.getId();

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
        connection.setUpdatedAt(LocalDateTime.now());
        connectionRepository.save(connection);

        ConnectionStatusDto dto = connectionMapper.toDto(connection);

        Profile requester = profileRepository.findById(actorProfileId)
                .orElseThrow(() -> new IllegalStateException("Requester profile missing"));

        dto.setRequesterPublicId(requester.getPublicId());
        dto.setRequesterName(requester.getName());
        dto.setRequesterPosition(requester.getPosition());
        dto.setRequesterProfilePicture(requester.getProfilePicture());
        return dto;
    }

    public void reject(Object principal, String publicId){
        Long actorProfileId = extractProfileIdFromPrincipal(principal);
        Profile profile = profileRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("User now found by publicId: " + publicId));
        Long targetProfileId = profile.getId();
        ConnectionId connectionId = orderConnectionPair(actorProfileId, targetProfileId);
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new IllegalArgumentException("No request exists"));

        connectionRepository.deleteById(connectionId);
    }

    public boolean isConnected(Object principal, ConnectionCheckDto checkDto){
        Long actorProfileId = extractProfileIdFromPrincipal(principal);
        Profile profile = profileRepository.findByPublicId(checkDto.getPublicId())
                .orElseThrow(() -> new RuntimeException("Profile not found by Id: " + checkDto.getPublicId()));
        Long targetProfileId = profile.getId();

        if (Objects.equals(actorProfileId, targetProfileId)){
            throw new IllegalArgumentException("Cannot check connection to yourself");
        }

        ConnectionId connectionId = orderConnectionPair(actorProfileId, targetProfileId);
        return connectionRepository.existsByConnectorIdAndConnecteeIdAndConnectionStatus(
                connectionId.getConnectorId(), connectionId.getConnecteeId(), ConnectionStatus.ACCEPTED);
    }


    public List<ConnectionListDto> listAccepted(Object principal){
        Long profileId = extractProfileIdFromPrincipal(principal);
        List<Connection> rows = connectionRepository.findAllAcceptedFor(profileId);

        return rows.stream().map(connection -> {
            Long senderId = connection.getRequesterId();
            Profile senderProfile = senderId.equals(connection.getConnectorId()) ? connection.getConnector() : connection.getConnectee();

            ConnectionListDto connectionListDto = new ConnectionListDto();
            connectionListDto.setSenderProfileId(senderId);
            connectionListDto.setSenderName(senderProfile.getName());
            connectionListDto.setSenderEmail(senderProfile.getUser().getEmail());
            connectionListDto.setSenderProfilePic(senderProfile.getProfilePicture());
            connectionListDto.setPublicId(senderProfile.getPublicId());
            return connectionListDto;
        }).toList();
    }


    public List<ConnectionListDto> listPending(Object principal){
        Long profileId = extractProfileIdFromPrincipal(principal);
        List<Connection> rows = connectionRepository.findAllPendingFor(profileId);

        return rows.stream().map(connection -> {
            Long senderId = connection.getRequesterId();
            Profile senderProfile = senderId.equals(connection.getConnectorId()) ? connection.getConnector() : connection.getConnectee();

            ConnectionListDto connectionPendingDto = new ConnectionListDto();
            connectionPendingDto.setSenderProfileId(senderId);
            connectionPendingDto.setSenderName(senderProfile.getName());
            connectionPendingDto.setSenderEmail(senderProfile.getUser().getEmail());
            connectionPendingDto.setSenderProfilePic(senderProfile.getProfilePicture());
            connectionPendingDto.setPublicId(senderProfile.getPublicId());
            return connectionPendingDto;
                }).toList();
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
