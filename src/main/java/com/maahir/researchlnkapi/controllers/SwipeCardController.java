package com.maahir.researchlnkapi.controllers;


import com.maahir.researchlnkapi.dtos.swipeCards.SwipeCardDto;
import com.maahir.researchlnkapi.dtos.swipeCards.UpdateSwipeCardRequest;
import com.maahir.researchlnkapi.model.entities.SwipeCard;
import com.maahir.researchlnkapi.services.SwipeCardService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/swipe-card")
public class SwipeCardController {
    private final SwipeCardService swipeCardService;

    @Autowired
    public SwipeCardController(SwipeCardService swipeCardService){
        this.swipeCardService = swipeCardService;
    }

    @PutMapping("/me")
    public ResponseEntity<SwipeCardDto> updateSwipeCard(Object principal, UpdateSwipeCardRequest request){
        SwipeCardDto updatedSwipeCard = swipeCardService.updateSwipeCard(principal, request);
        return ResponseEntity.ok(updatedSwipeCard);
    }

    @GetMapping("/me")
    public ResponseEntity<SwipeCardDto> getMySwipeCard(Object principal){
        SwipeCardDto mySwipeCard = swipeCardService.getMySwipeCard(principal);
        return ResponseEntity.ok(mySwipeCard);
    }
}
