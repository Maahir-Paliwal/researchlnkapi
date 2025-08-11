package com.maahir.researchlnkapi.controllers;


import com.maahir.researchlnkapi.dtos.profiles.ProfileDto;
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

    //Get the current authenticated user's profile
    @GetMapping("/me")
    public ResponseEntity<ProfileDto> getMyProfile(@AuthenticationPrincipal Object principal){
        ProfileDto profile = profileService.getMyProfile(principal);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/me")
    public ResponseEntity<ProfileDto> updateMyProfile(@AuthenticationPrincipal Object principal,
                                                      @RequestBody UpdateProfileRequest request){
        ProfileDto updatedProfile = profileService.updateProfile(principal, request);
        return ResponseEntity.ok(updatedProfile);
    }
}
