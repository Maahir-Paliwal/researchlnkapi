package com.maahir.researchlnkapi.services;


import com.maahir.researchlnkapi.dtos.users.RegisterUserRequest;
import com.maahir.researchlnkapi.dtos.users.UpdateUserRequest;
import com.maahir.researchlnkapi.dtos.users.UserDto;
import com.maahir.researchlnkapi.mappers.UserMapper;
import com.maahir.researchlnkapi.model.entities.User;
import com.maahir.researchlnkapi.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

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

    public UserDto registerUser(RegisterUserRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email is already registered");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserDto getUserByEmail(String email){
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toDto(user);
    }

    public UserDto updateUser(String email, UpdateUserRequest request){
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        userMapper.update(request, user);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    public UserDto findByOrcidId(String orcidId){
        User user = userRepository.findByOrcidId(orcidId).
                orElseThrow(() -> new RuntimeException("User not found by OrcidId"));
        return userMapper.toDto(user);
    }

    public UserDto processOrcidLogin(String orcidId, String email){
        Optional<User> userOpt = userRepository.findByEmail(email);

        User user;
        if (userOpt.isPresent()){
            user = userOpt.get();
        } else {
            user = User.builder()
                    .orcidId(orcidId)
                    .email(email)
                    .build();
            user = userRepository.save(user);
        }
        return userMapper.toDto(user);
    }

    public void deleteUserByEmail(String email){
        if (!userRepository.existsByEmail(email)){
            throw new RuntimeException("User cannot be deleted because email:" + email + "was not found");
        }

        Long id = extractUserIdFromEmail(email);
        userRepository.deleteById(id);
    }


    private User extractUserFromPrincipal(Object principal){
        String email;
        if (principal instanceof OAuth2User oauthUser){
            email = oauthUser.getAttribute("email");
        } else if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else {
            throw new RuntimeException("Unsupported principal type: " + principal.getClass().getSimpleName());
        }
        return userRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("User not found by email"));
    }


    private Long extractUserIdFromEmail(String email) {
        User user = userRepository.findByEmail(email).
                orElseThrow(()-> new RuntimeException("User not found with email: " + email));

        return user.getId();
    }
}
