package com.maahir.researchlnkapi.controllers;

import com.maahir.researchlnkapi.dtos.users.RegisterUserByPassword;
import com.maahir.researchlnkapi.dtos.users.UserDto;
import com.maahir.researchlnkapi.mappers.UserMapper;
import com.maahir.researchlnkapi.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public AuthController(AuthenticationManager authenticationManager, UserMapper userMapper) {
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;

    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody RegisterUserByPassword request){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        UserDto userDto = userMapper.toDto(userDetails.getUserEntity());
        return ResponseEntity.ok(userDto);

    }
}
