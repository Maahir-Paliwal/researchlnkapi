package com.maahir.researchlnkapi.security;

import com.maahir.researchlnkapi.model.entities.Profile;
import com.maahir.researchlnkapi.model.entities.SwipeCard;
import com.maahir.researchlnkapi.model.entities.User;
import com.maahir.researchlnkapi.model.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    public CustomOAuth2UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(oAuth2UserRequest);
        String orcidId = oAuth2User.getAttribute("sub");

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String email = (String) request.getSession().getAttribute("ORCID_EMAIL");

        if (email == null) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("invalid request"),
                    "Email must be provided before starting ORCID flow");
        }

        User user = userRepository.findByOrcidId(orcidId)
                .or(() -> userRepository.findByEmail(email))
                .map(u -> {
                    if (u.getOrcidId() == null) {
                        u.setOrcidId(orcidId);
                        return userRepository.save(u);
                    }
                    return u;
                })
                .orElseGet(()-> {
                    Profile profile = Profile.builder()                             //create an empty profile
                            .name("")
                            .position("")
                            .description("")
                            .profilePicture("")
                            .build();

                    SwipeCard swipeCard = SwipeCard.builder()                       //create an empty swipeCard
                            .name("")
                            .position("")
                            .description("")
                            .build();

                    User newUser = User.builder()
                            .email(email)
                            .orcidId(orcidId)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();
                    newUser.setProfile(profile);
                    newUser.getProfile().setSwipeCard(swipeCard);
                    return userRepository.save(newUser);
                });

        return new DefaultOAuth2User(
                List.of(),
                Map.of("id", user.getId(),
                        "email", user.getEmail(),
                        "orcidId", user.getOrcidId()),
                        "email"
                );
    }
}
