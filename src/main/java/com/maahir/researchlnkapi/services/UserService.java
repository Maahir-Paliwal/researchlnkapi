package com.maahir.researchlnkapi.services;


import com.maahir.researchlnkapi.dtos.users.RegisterUserByOrcid;
import com.maahir.researchlnkapi.dtos.users.RegisterUserByPassword;
import com.maahir.researchlnkapi.dtos.users.UpdateUserRequest;
import com.maahir.researchlnkapi.dtos.users.UserDto;
import com.maahir.researchlnkapi.mappers.UserMapper;
import com.maahir.researchlnkapi.model.entities.Profile;
import com.maahir.researchlnkapi.model.entities.User;
import com.maahir.researchlnkapi.model.repositories.UserRepository;
import com.maahir.researchlnkapi.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }


    public UserDto registerUserByEmailAndPassword(RegisterUserByPassword request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email is already registered");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        //Create an empty profile and link it to the user
        Profile profile = Profile.builder()
                .name("")
                .position("")
                .description("")
                .profilePicture("")
                .build();

        user.setProfile(profile);                                   //handling bidirectionality

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }


    public UserDto registerUserByOrcid(RegisterUserByOrcid request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email is already registered");
        }
        User user = userMapper.toEntity(request);

        Profile profile = Profile.builder()
                .name("")
                .position("")
                .description("")
                .profilePicture("")
                .build();

        user.setProfile(profile);                                   //handling bidirectionality
        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }


    public UserDto getUser(Object principal){
        User user = extractUserFromPrincipal(principal);
        return userMapper.toDto(user);
    }


    public UserDto updateUser(Object principal, UpdateUserRequest request){
        User user = extractUserFromPrincipal(principal);
        userMapper.update(request, user);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }


    public void deleteUser(Object principal){
        User user = extractUserFromPrincipal(principal);
        userRepository.delete(user);
    }


    private User extractUserFromPrincipal(Object principal){
        if (principal instanceof OAuth2User oauthUser){
            String orcidId = oauthUser.getAttribute("orcidId");
            return userRepository.findByOrcidId(orcidId).
                    orElseThrow(() -> new RuntimeException("User not found by orcidId"));

        } else if (principal instanceof CustomUserDetails customUserDetails) {
            String email = customUserDetails.getUsername();                   //is getUsername() the right method to call?
            return userRepository.findByEmail(email).
                    orElseThrow(() -> new RuntimeException("User not found by email"));

        } else {
            throw new RuntimeException("Unsupported principal type: " + principal.getClass().getSimpleName());
        }
    }
}
