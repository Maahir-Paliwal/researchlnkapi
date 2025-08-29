package com.maahir.researchlnkapi.controllers;


import com.maahir.researchlnkapi.dtos.swipeCards.PublicSwipeCardDto;
import com.maahir.researchlnkapi.dtos.swipeCards.SwipeCardDto;
import com.maahir.researchlnkapi.dtos.swipeCards.UpdateSwipeCardRequest;
import com.maahir.researchlnkapi.model.entities.SwipeCard;
import com.maahir.researchlnkapi.services.SwipeCardService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/swipe-card")
public class SwipeCardController {
    private final SwipeCardService swipeCardService;

    @Autowired
    public SwipeCardController(SwipeCardService swipeCardService){
        this.swipeCardService = swipeCardService;
    }

    //------------------------READ ONLY --------------------------
    @GetMapping("/me")
    public ResponseEntity<SwipeCardDto> getMySwipeCard(@AuthenticationPrincipal Object principal){
        SwipeCardDto mySwipeCard = swipeCardService.getMySwipeCard(principal);
        return ResponseEntity.ok(mySwipeCard);
    }

    //goes hand in hand with getPublicRelevantExperiences() in RelevantExperienceController
    @GetMapping("{publicId}")
    public ResponseEntity<PublicSwipeCardDto> getPublicSwipeCard(@AuthenticationPrincipal Object principal,
                                                                 @PathVariable String publicId){
        PublicSwipeCardDto publicSwipeCard = swipeCardService.getPublicSwipeCard(principal, publicId);
        return ResponseEntity.ok(publicSwipeCard);
    }

    //----------------------- UPDATE ---------------------------
    @PutMapping("/me")
    public ResponseEntity<SwipeCardDto> updateSwipeCard(@AuthenticationPrincipal Object principal,
                                                        @RequestBody UpdateSwipeCardRequest request){
        SwipeCardDto updatedSwipeCard = swipeCardService.updateSwipeCard(principal, request);
        return ResponseEntity.ok(updatedSwipeCard);
    }

}
