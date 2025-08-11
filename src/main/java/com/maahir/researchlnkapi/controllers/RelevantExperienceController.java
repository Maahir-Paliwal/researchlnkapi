package com.maahir.researchlnkapi.controllers;

import com.maahir.researchlnkapi.dtos.relevantExperiences.RelevantExperienceDto;
import com.maahir.researchlnkapi.dtos.relevantExperiences.RelevantExperienceRequest;
import com.maahir.researchlnkapi.services.RelevantExperienceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relevant-experience")
public class RelevantExperienceController {
    private final RelevantExperienceService relevantExperienceService;

    @Autowired
    public RelevantExperienceController(RelevantExperienceService relevantExperienceService){
        this.relevantExperienceService = relevantExperienceService;
    }

    //------------------ READ ONLY -----------------------
    @GetMapping("/me")
    public ResponseEntity<List<RelevantExperienceDto>> getMyRelevantExperiences(@AuthenticationPrincipal Object principal){
        List<RelevantExperienceDto> dtoList = relevantExperienceService.listMyExperiences(principal);
        return ResponseEntity.ok(dtoList);
    }

    //---------- ANOTHER METHOD FOR VIEWING ANOTHER PROFILE'S EXPERIENCES SHOULD GO HERE --------------


    //------------------- CREATE --------------------------
    @PostMapping("/me")
    public ResponseEntity<RelevantExperienceDto> createRelevantExperience(@AuthenticationPrincipal Object principal,
                                                                          @RequestBody RelevantExperienceRequest relevantExperiencerequest){
        RelevantExperienceDto createdExperience = relevantExperienceService.createNewRelevantExperience(principal, relevantExperiencerequest);
        return ResponseEntity.ok(createdExperience);
    }


    //------------------- UPDATE ---------------------------
    @PutMapping("/{id}")
    public ResponseEntity<RelevantExperienceDto> updateRelevantExperience(@AuthenticationPrincipal Object principal,
                                                                          @PathVariable("id") Long experienceId,
                                                                          @RequestBody RelevantExperienceRequest relevantExperiencerequest){
        RelevantExperienceDto updatedExperience = relevantExperienceService.updateRelevantExperience(principal, experienceId, relevantExperiencerequest);
        return ResponseEntity.ok(updatedExperience);
    }


    //-------------------- DELETE ----------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelevantExperience(@AuthenticationPrincipal Object principal,
                                                         @PathVariable("id") Long experienceId){
        relevantExperienceService.deleteMyRelevantExperience(principal, experienceId);
        return ResponseEntity.noContent().build();
    }

}
