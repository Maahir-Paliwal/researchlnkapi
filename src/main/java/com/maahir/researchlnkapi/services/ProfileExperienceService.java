package com.maahir.researchlnkapi.services;

import com.maahir.researchlnkapi.dtos.ProfileExperience.ProfileExperienceCreateRequest;
import com.maahir.researchlnkapi.dtos.ProfileExperience.ProfileExperienceDto;
import com.maahir.researchlnkapi.dtos.ProfileExperience.ProfileExperienceUpdateRequest;
import com.maahir.researchlnkapi.mappers.ProfileExperienceMapper;
import com.maahir.researchlnkapi.model.entities.Profile;
import com.maahir.researchlnkapi.model.entities.ProfileExperience;
import com.maahir.researchlnkapi.model.entities.User;
import com.maahir.researchlnkapi.model.repositories.ProfileExperienceRepository;
import com.maahir.researchlnkapi.model.repositories.ProfileRepository;
import com.maahir.researchlnkapi.model.repositories.UserRepository;
import com.maahir.researchlnkapi.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class ProfileExperienceService {
    private final ProfileExperienceRepository profileExperienceRepository;
    private final ProfileExperienceMapper profileExperienceMapper;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public ProfileExperienceService(ProfileExperienceRepository profileExperienceRepository,
                                    ProfileExperienceMapper profileExperienceMapper,
                                    UserRepository userRepository, ProfileRepository profileRepository){
        this.profileExperienceRepository = profileExperienceRepository;
        this.profileExperienceMapper = profileExperienceMapper;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }



    // ------------------- READ ONLY METHODS ------------------
    public List<ProfileExperienceDto> listMyProfileExperiences(Object principal){
        Long profileId = currentProfileId(principal);
        return profileExperienceMapper.toDtoList(profileExperienceRepository.findOrderedByProfileId(profileId));
    }

    public List<ProfileExperienceDto> listProfileExperiencesByProfileId(Long profileId){
        return profileExperienceMapper.toDtoList(profileExperienceRepository.findOrderedByProfileId(profileId));
    }



    // ----------------- CREATE METHODS ------------------
    public ProfileExperienceDto createNewProfileExperience(Object principal, ProfileExperienceCreateRequest request){
        Long profileId = currentProfileId(principal);

        // Basic required fields check
        if (isBlank(request.getTitle())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required");
        if (isBlank(request.getStartAt())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date (yyyy-MM) is required");
        if (isBlank(request.getDescription())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description is required");

        LocalDateTime startAt = toStartOfMonth(request.getStartAt());
        LocalDateTime endAt = toNullableStartOfMonth(request.getEndAt());

        if (endAt != null && endAt.isBefore(startAt)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date cannot be before start date");
        }

        User user = extractUserFromPrincipal(principal);
        Profile profile = user.getProfile();
        if (profile == null || !profile.getId().equals(profileId)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile not initialized");
        }

        ProfileExperience profileExperience = ProfileExperience.builder()
                .profile(profile)
                .title(request.getTitle().trim())
                .description(request.getDescription().trim())
                .startAt(startAt)
                .endAt(endAt)
                .build();

        profileExperience = profileExperienceRepository.save(profileExperience);
        return profileExperienceMapper.toDto(profileExperience);
    }



    // ----------------- UPDATE METHODS ------------------
    public ProfileExperienceDto updateProfileExperience(Object principal,
                                                        Long profileExperienceId,
                                                        ProfileExperienceUpdateRequest request){
        Long myProfileId = currentProfileId(principal);

        ProfileExperience profileExperience = profileExperienceRepository.findById(profileExperienceId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile Experience not found by Id: " + profileExperienceId));


    }

    // ----------------- HELPER METHODS ------------------
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
