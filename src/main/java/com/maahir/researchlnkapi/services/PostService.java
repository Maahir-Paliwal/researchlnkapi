package com.maahir.researchlnkapi.services;

import com.maahir.researchlnkapi.dtos.posts.PostCreateRequest;
import com.maahir.researchlnkapi.dtos.posts.PostDto;
import com.maahir.researchlnkapi.dtos.posts.PostUpdateRequest;
import com.maahir.researchlnkapi.mappers.PostMapper;
import com.maahir.researchlnkapi.model.entities.Post;
import com.maahir.researchlnkapi.model.entities.Profile;
import com.maahir.researchlnkapi.model.entities.User;
import com.maahir.researchlnkapi.model.repositories.PostRepository;
import com.maahir.researchlnkapi.model.repositories.ProfileRepository;
import com.maahir.researchlnkapi.model.repositories.UserRepository;
import com.maahir.researchlnkapi.security.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PostMapper postMapper;

    @Autowired
    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       PostMapper postMapper,
                       ProfileRepository profileRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
        this.profileRepository = profileRepository;
    }

    // --------------- READ ONLY ---------------
    public List<PostDto> listMyPosts(Object principal){
        Long profileId = currentProfileId(principal);
        return postMapper.toDtoList(postRepository.findOrderedByProfileId(profileId));
    }

    //Add another method to view any user's posts from their profile


    //----------------- CREATE ------------------
    public PostDto createNewPost(Object principal, PostCreateRequest request){
        Long profileId = currentProfileId(principal);

        if (isBlank(request.getDescription())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description is required");
        if (request.getViewType() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "View type is required");

        Post post = postMapper.toEntity(request);
        post.setProfile(profileRepository.findById(profileId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"))
        );
        post.setCreatedAt(LocalDateTime.now());

        post = postRepository.save(post);
        return postMapper.toDto(post);
    }


    //----------------- UPDATE -------------------
    public PostDto updateMyPost(Object principal, Long postId, PostUpdateRequest request){
        Long myProfileId = currentProfileId(principal);

        if (isBlank(request.getDescription())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description is required");
        if (request.getViewType() == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "View type is required");

        Post post = postRepository.findWithProfileById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found by Id: " + postId));

        if (!post.getProfile().getId().equals(myProfileId)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not own this post");

        postMapper.update(request, post);
        Post updatedPost = postRepository.save(post);
        return postMapper.toDto(updatedPost);
    }



    // ---------------- DELETE -------------------
    @Transactional
    public void deleteMyPost(Object principal, Long postId){
        Long myProfileId = currentProfileId(principal);
        int rows = postRepository.deleteByIdAndProfile_Id(postId, myProfileId);
        if (rows == 0) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found by Id: " + postId);
    }


    // ---------------- HELPERS ------------------
    private boolean isBlank(String str){
        return str == null || str.isBlank();
    }

    private Long currentProfileId(Object principal){
        User user = extractUserFromPrincipal(principal);
        Profile profile = user.getProfile();
        if (profile == null){
            throw new RuntimeException("Profile not initialized");
        }
        return profile.getId();
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
