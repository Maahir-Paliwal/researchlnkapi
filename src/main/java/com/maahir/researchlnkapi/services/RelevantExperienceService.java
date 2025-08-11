package com.maahir.researchlnkapi.services;

import com.maahir.researchlnkapi.dtos.relevantExperiences.RelevantExperienceDto;
import com.maahir.researchlnkapi.dtos.relevantExperiences.RelevantExperienceRequest;
import com.maahir.researchlnkapi.mappers.RelevantExperienceMapper;
import com.maahir.researchlnkapi.model.entities.Profile;
import com.maahir.researchlnkapi.model.entities.RelevantExperience;
import com.maahir.researchlnkapi.model.entities.User;
import com.maahir.researchlnkapi.model.repositories.RelevantExperienceRepository;
import com.maahir.researchlnkapi.model.repositories.UserRepository;
import com.maahir.researchlnkapi.security.CustomUserDetails;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
public class RelevantExperienceService {
    private final RelevantExperienceRepository relevantExperienceRepository;
    private final UserRepository userRepository;
    private final RelevantExperienceMapper relevantExperienceMapper;

    @Autowired
    public RelevantExperienceService(RelevantExperienceRepository relevantExperienceRepository,
                                     UserRepository userRepository,
                                     RelevantExperienceMapper relevantExperienceMapper){
        this.relevantExperienceRepository = relevantExperienceRepository;
        this.userRepository = userRepository;
        this.relevantExperienceMapper = relevantExperienceMapper;
    }



    //--------------------- READ ONLY METHODS --------------------
    //for display on your page
    public List<RelevantExperienceDto> listMyExperiences(Object principal){
        Long cardId = currentSwipeCardId(principal);
        return relevantExperienceMapper.toDtoList(relevantExperienceRepository.findOrderedBySwipeCardId(cardId));
    }



    //--------------------- CREATE --------------------------------
    public RelevantExperienceDto createNewRelevantExperience(Object principal, RelevantExperienceRequest request){
        Long cardId = currentSwipeCardId(principal);

        long count = relevantExperienceRepository.countBySwipeCard_Id(cardId);
        if (count >= 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can only have 3 experiences");
        }

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
        if (profile == null || profile.getSwipeCard() == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Swipe card not initialized");
        }

        RelevantExperience relevantExperience = new RelevantExperience();
        relevantExperience.setSwipeCard(profile.getSwipeCard());
        relevantExperience.setTitle(request.getTitle().trim());
        relevantExperience.setDescription(request.getDescription().trim());
        relevantExperience.setStartAt(startAt);
        relevantExperience.setEndAt(endAt);

        relevantExperience = relevantExperienceRepository.save(relevantExperience);
        return relevantExperienceMapper.toDto(relevantExperience);

    }



    //---------------UPDATE------------------
    public RelevantExperienceDto updateRelevantExperience(Object principal,
                                                          Long experienceId,
                                                          RelevantExperienceRequest request){
        Long myCardId = currentSwipeCardId(principal);

        RelevantExperience relevantExperience = relevantExperienceRepository.findWithCardById(experienceId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Relevant Experience not found by Id: " + experienceId));

        if(!relevantExperience.getSwipeCard().getId().equals(myCardId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this experience");
        }

        if (isBlank(request.getTitle())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required");
        if (isBlank(request.getStartAt())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date (yyyy-MM) is required");
        if (isBlank(request.getDescription())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description is required");

        LocalDateTime startAt = toStartOfMonth(request.getStartAt());
        LocalDateTime endAt = toNullableStartOfMonth(request.getEndAt());

        if (endAt != null && endAt.isBefore(startAt)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date cannot be before start date");
        }

        relevantExperience.setTitle(request.getTitle().trim());
        relevantExperience.setDescription(request.getDescription().trim());
        relevantExperience.setStartAt(startAt);
        relevantExperience.setEndAt(endAt);

        relevantExperience = relevantExperienceRepository.save(relevantExperience);
        return relevantExperienceMapper.toDto(relevantExperience);

    }

    //-------------- DELETE ---------------------------
    public void deleteMyRelevantExperience(Object principal, Long experienceId){
        Long myCardId = currentSwipeCardId(principal);
        int rows = relevantExperienceRepository.deleteByIdAndSwipeCard_Id(experienceId, myCardId);
        if (rows == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Relevant Experience not found by Id: " + experienceId);
    }



    //------------- HELPER FUNCTIONS ------------------

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

    private Long currentSwipeCardId(Object principal){
        User user = extractUserFromPrincipal(principal);
        return user.getProfile().getSwipeCard().getId();
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
