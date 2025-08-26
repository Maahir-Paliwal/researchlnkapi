package com.maahir.researchlnkapi.controllers;


import com.maahir.researchlnkapi.dtos.profiles.ProfileDto;
import com.maahir.researchlnkapi.dtos.profiles.PublicProfileDto;
import com.maahir.researchlnkapi.dtos.profiles.UpdateProfileRequest;
import com.maahir.researchlnkapi.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }

    // ----------------------- READ ONLY ------------------------

    //Get the current authenticated user's profile
    //This controller should be called on ResearchLNK/profile/me
    @GetMapping("/me")
    public ResponseEntity<ProfileDto> getMyProfile(@AuthenticationPrincipal Object principal){
        ProfileDto profile = profileService.getMyProfile(principal);
        return ResponseEntity.ok(profile);
    }

    //This controller should be called on ResearchLNK/profile/[publicId]
    //This API call should be paired with listPublicProfileExperiences() in ProfileExperienceController
    @GetMapping("/{publicId}")
    public ResponseEntity<PublicProfileDto> getPublicProfile(@AuthenticationPrincipal Object principal,
                                                             @PathVariable String publicId){
        PublicProfileDto publicProfile = profileService.getPublicProfile(principal, publicId);
        return ResponseEntity.ok(publicProfile);
    }

    // --------------------- UPDATE ----------------------

    @PutMapping("/me")
    public ResponseEntity<ProfileDto> updateMyProfile(@AuthenticationPrincipal Object principal,
                                                      @RequestBody UpdateProfileRequest request){
        ProfileDto updatedProfile = profileService.updateProfile(principal, request);
        return ResponseEntity.ok(updatedProfile);
    }
}
