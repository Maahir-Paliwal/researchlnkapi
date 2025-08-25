package com.maahir.researchlnkapi.controllers;

import com.maahir.researchlnkapi.dtos.ProfileExperience.ProfileExperienceCreateRequest;
import com.maahir.researchlnkapi.dtos.ProfileExperience.ProfileExperienceDto;
import com.maahir.researchlnkapi.dtos.ProfileExperience.ProfileExperienceUpdateRequest;
import com.maahir.researchlnkapi.services.ProfileExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile-experience")
public class ProfileExperienceController {
    private final ProfileExperienceService profileExperienceService;

    @Autowired
    public ProfileExperienceController(ProfileExperienceService profileExperienceService){
        this.profileExperienceService = profileExperienceService;
    }

    // -------------------- READ ONLY ---------------------
    @GetMapping("/me")
    public ResponseEntity<List<ProfileExperienceDto>> getMyProfileExperiences(@AuthenticationPrincipal Object principal){
        List<ProfileExperienceDto> myProfileExperiences = profileExperienceService.listMyProfileExperiences(principal);
        return ResponseEntity.ok(myProfileExperiences);
    }

    //Another method for viewing other people's profile experiences should go here


    // --------------------- CREATE ------------------------
    @PostMapping("/me")
    public ResponseEntity<ProfileExperienceDto> createProfileExperience(@AuthenticationPrincipal Object principal,
                                                                        @RequestBody ProfileExperienceCreateRequest request){
        ProfileExperienceDto createdExperience = profileExperienceService.createNewProfileExperience(principal, request);
        return ResponseEntity.ok(createdExperience);
    }

    // --------------------- UPDATE ------------------------
    @PutMapping("/{id}")
    public ResponseEntity<ProfileExperienceDto> updateProfileExperience(@AuthenticationPrincipal Object principal,
                                                                        @PathVariable("id") Long profileExperienceId,
                                                                        @RequestBody ProfileExperienceUpdateRequest request){
        ProfileExperienceDto updatedExperience = profileExperienceService.updateProfileExperience(principal, profileExperienceId, request);
        return ResponseEntity.ok(updatedExperience);
    }

    // --------------------- DELETE ------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfileExperience(@AuthenticationPrincipal Object principal,
                                                        @PathVariable("id") Long profileExperienceId){
        profileExperienceService.deleteMyProfileExperience(principal, profileExperienceId);
        return ResponseEntity.noContent().build();
    }
}
