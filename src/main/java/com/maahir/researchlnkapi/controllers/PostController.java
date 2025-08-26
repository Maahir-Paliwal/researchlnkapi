package com.maahir.researchlnkapi.controllers;

import com.maahir.researchlnkapi.dtos.posts.PostCreateRequest;
import com.maahir.researchlnkapi.dtos.posts.PostDto;
import com.maahir.researchlnkapi.dtos.posts.PostUpdateRequest;
import com.maahir.researchlnkapi.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    //--------------- READ ONLY ------------------
    @GetMapping("/me")
    public ResponseEntity<List<PostDto>> listMyPosts(@AuthenticationPrincipal Object principal){
        List<PostDto> myPosts = postService.listMyPosts(principal);
        return ResponseEntity.ok(myPosts);
    }

    //missing one controller for viewing other's posts

    //---------------- CREATE --------------------
    @PostMapping("/me")
    public ResponseEntity<PostDto> createMyPost(@AuthenticationPrincipal Object principal,
                                @RequestBody PostCreateRequest request){
        PostDto postDto = postService.createNewPost(principal, request);
        return ResponseEntity.ok(postDto);
    }

    //---------------- UPDATE ----------------------
    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updateMyPost(@AuthenticationPrincipal Object principal,
                                @PathVariable Long id,
                                @RequestBody PostUpdateRequest request){
        PostDto postDto = postService.updateMyPost(principal, id, request);
        return ResponseEntity.ok(postDto);
    }

    //----------------- DELETE ----------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyPost(@AuthenticationPrincipal Object principal,
                                             @PathVariable Long id){
        postService.deleteMyPost(principal, id);
        return ResponseEntity.noContent().build();
    }

}
