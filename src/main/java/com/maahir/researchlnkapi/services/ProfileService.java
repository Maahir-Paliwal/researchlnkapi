package com.maahir.researchlnkapi.services;


import com.maahir.researchlnkapi.dtos.profiles.ProfileDto;
import com.maahir.researchlnkapi.dtos.profiles.UpdateProfileRequest;
import com.maahir.researchlnkapi.mappers.ProfileMapper;
import com.maahir.researchlnkapi.model.entities.Profile;
import com.maahir.researchlnkapi.model.entities.User;
import com.maahir.researchlnkapi.model.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;

    @Autowired
    public ProfileService(UserRepository userRepository, ProfileMapper profileMapper){
        this.userRepository = userRepository;
        this.profileMapper = profileMapper;
    }

    public ProfileDto getProfile(Object principal) {
        User user = extractUserFromPrincipal(principal);
        Profile profile = user.getProfile();

        return profileMapper.toDto(profile);
    }

    public ProfileDto updateProfile(Object principal, UpdateProfileRequest request){
        User user = extractUserFromPrincipal(principal);
        Profile profile = user.getProfile();
        profileMapper.update(request, profile);
        return profileMapper.toDto(profile);
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
}
