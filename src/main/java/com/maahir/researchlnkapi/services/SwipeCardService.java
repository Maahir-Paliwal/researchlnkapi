package com.maahir.researchlnkapi.services;

import com.maahir.researchlnkapi.dtos.swipeCards.SwipeCardDto;
import com.maahir.researchlnkapi.dtos.swipeCards.UpdateSwipeCardRequest;
import com.maahir.researchlnkapi.mappers.SwipeCardMapper;
import com.maahir.researchlnkapi.model.entities.SwipeCard;
import com.maahir.researchlnkapi.model.entities.User;
import com.maahir.researchlnkapi.model.repositories.SwipeCardRepository;
import com.maahir.researchlnkapi.model.repositories.UserRepository;
import com.maahir.researchlnkapi.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class SwipeCardService {
    private final SwipeCardRepository swipeCardRepository;
    private final UserRepository userRepository;
    private final SwipeCardMapper swipeCardMapper;

    @Autowired
    public SwipeCardService(SwipeCardRepository swipeCardRepository, UserRepository userRepository, SwipeCardMapper swipeCardMapper){
        this.swipeCardRepository = swipeCardRepository;
        this.userRepository = userRepository;
        this.swipeCardMapper = swipeCardMapper;
    }

    public SwipeCardDto getMySwipeCard(Object principal) {
        User user = extractUserFromPrincipal(principal);
        SwipeCard swipeCard = user.getProfile().getSwipeCard();
        return swipeCardMapper.toDto(swipeCard);
    }

    public SwipeCardDto updateSwipeCard(Object principal, UpdateSwipeCardRequest request){
        User user = extractUserFromPrincipal(principal);
        SwipeCard swipeCard = user.getProfile().getSwipeCard();
        swipeCardMapper.update(request, swipeCard);
        swipeCard = swipeCardRepository.save(swipeCard);
        return swipeCardMapper.toDto(swipeCard);
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
