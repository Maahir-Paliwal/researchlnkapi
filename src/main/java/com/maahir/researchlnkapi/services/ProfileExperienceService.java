package com.maahir.researchlnkapi.services;

import com.maahir.researchlnkapi.mappers.ProfileExperienceMapper;
import com.maahir.researchlnkapi.model.entities.User;
import com.maahir.researchlnkapi.model.repositories.ProfileExperienceRepository;
import com.maahir.researchlnkapi.model.repositories.UserRepository;
import com.maahir.researchlnkapi.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.print.attribute.standard.JobKOctets;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Service
public class ProfileExperienceService {
    private final ProfileExperienceRepository profileExperienceRepository;
    private final ProfileExperienceMapper profileExperienceMapper;
    private final UserRepository userRepository;

    @Autowired
    public ProfileExperienceService(ProfileExperienceRepository profileExperienceRepository,
                                    ProfileExperienceMapper profileExperienceMapper,
                                    UserRepository userRepository){
        this.profileExperienceRepository = profileExperienceRepository;
        this.profileExperienceMapper = profileExperienceMapper;
        this.userRepository = userRepository;
    }




    // ------------------ HELPER METHODS ------------------
    private boolean isBlank(String str){
        return str == null || str.trim().isEmpty();
    }

    private LocalDateTime toStartOfMonth(String stringDate){
        try {
            return YearMonth.parse(stringDate.trim()).atDay(1).atStartOfDay();
        } catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format: " + stringDate + ". Expected format: yyyy-MM");
        }
    }

    private LocalDateTime toNullableStartOfMonth(String stringDate){
        if (stringDate == null) return null;
        String trimmedDate = stringDate.trim();
        if (trimmedDate.isEmpty()) return null;
        return toStartOfMonth(trimmedDate);
    }

    private long currentProfileId(Object principal){
        User user = extractUserFromPrincipal(principal);
        return user.getProfile().getId();
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
