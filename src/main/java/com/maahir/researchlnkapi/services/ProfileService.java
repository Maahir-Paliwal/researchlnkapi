package com.maahir.researchlnkapi.services;


import com.maahir.researchlnkapi.dtos.profiles.ProfileDto;
import com.maahir.researchlnkapi.dtos.profiles.PublicProfileDto;
import com.maahir.researchlnkapi.dtos.profiles.UpdateProfileRequest;
import com.maahir.researchlnkapi.mappers.ProfileMapper;
import com.maahir.researchlnkapi.model.entities.Profile;
import com.maahir.researchlnkapi.model.entities.User;
import com.maahir.researchlnkapi.model.repositories.ProfileRepository;
import com.maahir.researchlnkapi.model.repositories.UserRepository;
import com.maahir.researchlnkapi.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;
    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileService(UserRepository userRepository, ProfileMapper profileMapper, ProfileRepository profileRepository){
        this.userRepository = userRepository;
        this.profileMapper = profileMapper;
        this.profileRepository = profileRepository;
    }

    // ----------------- READ ONLY ------------------
    public ProfileDto getMyProfile(Object principal) {
        User user = extractUserFromPrincipal(principal);
        Profile profile = user.getProfile();

        return profileMapper.toDto(profile);
    }

    public PublicProfileDto getPublicProfile(Object principal, String publicId){
        Profile profile = profileRepository.findByPublicId(publicId)
                .orElseThrow(() -> new RuntimeException("Profile not found by Id: " + publicId));

        User user = extractUserFromPrincipal(principal);
        boolean owner = publicId.equals(user.getProfile().getPublicId());

        return profileMapper.toDto(profile, owner);
    }

    // ------------------- UPDATE ---------------------
    public ProfileDto updateProfile(Object principal, UpdateProfileRequest request){
        User user = extractUserFromPrincipal(principal);
        Profile profile = user.getProfile();
        profileMapper.update(request, profile);
        profile = profileRepository.save(profile);
        return profileMapper.toDto(profile);
    }

    private User extractUserFromPrincipal(Object principal){
        Long userId;
        if (principal instanceof OAuth2User oauthUser){
            Object rawId = oauthUser.getAttribute("id");
            if (rawId instanceof Number n) {
                userId = n.longValue();
            } else {
                userId = Long.valueOf(rawId.toString());
            }
            return userRepository.findById(userId).
                    orElseThrow(() -> new RuntimeException("User not found by Id"));

        } else if (principal instanceof CustomUserDetails customUserDetails) {
            userId = customUserDetails.getUserEntity().getId();
            return userRepository.findById(userId).
                    orElseThrow(() -> new RuntimeException("User not found by Id"));

        } else {
            throw new RuntimeException("Unsupported principal type: " + principal.getClass().getSimpleName());
        }
    }
}
