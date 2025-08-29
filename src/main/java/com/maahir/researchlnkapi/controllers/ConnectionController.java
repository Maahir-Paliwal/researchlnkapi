package com.maahir.researchlnkapi.controllers;

import com.maahir.researchlnkapi.dtos.connections.*;
import com.maahir.researchlnkapi.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {
    private final ConnectionService connectionService;

    @Autowired
    public ConnectionController(ConnectionService connectionService){
        this.connectionService = connectionService;
    }

    @PostMapping("/request/{publicId}")
    public ResponseEntity<ConnectionStatusDto> requestConnection(@AuthenticationPrincipal Object principal,
                                                                 @PathVariable String publicId){
        ConnectionStatusDto result = connectionService.request(principal, publicId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/accept/{publicId}")
    public ResponseEntity<ConnectionStatusDto> acceptRequest(@AuthenticationPrincipal Object principal,
                                                             @PathVariable String publicId){
        ConnectionStatusDto result = connectionService.accept(principal, publicId);
        return ResponseEntity.ok(result);
    }

    //reject can be used to reject a request or disconnect with someone
    @PostMapping("/reject/{publicId}")
    public ResponseEntity<Void> rejectRequest(@AuthenticationPrincipal Object principal,
                                              @PathVariable String publicId){
        connectionService.reject(principal, publicId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/are-connected")
    public ResponseEntity<Boolean> areConnected(@AuthenticationPrincipal Object principal,
                                                @RequestParam ConnectionCheckDto checkDto){
        boolean connected = connectionService.isConnected(principal, checkDto);
        return ResponseEntity.ok(connected);
    }

    @GetMapping("/accepted-list")
    public ResponseEntity<List<ConnectionListDto>> listAccepted(@AuthenticationPrincipal Object principal){
        List<ConnectionListDto> result = connectionService.listAccepted(principal);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pending-list")
    public ResponseEntity<List<ConnectionListDto>> listPending(@AuthenticationPrincipal Object principal){
        List<ConnectionListDto> result = connectionService.listPending(principal);
        return ResponseEntity.ok(result);
    }
}
