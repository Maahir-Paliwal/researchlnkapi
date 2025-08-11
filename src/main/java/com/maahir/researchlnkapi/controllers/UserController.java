package com.maahir.researchlnkapi.controllers;

import com.maahir.researchlnkapi.dtos.users.RegisterUserByPassword;
import com.maahir.researchlnkapi.dtos.users.UpdateUserRequest;
import com.maahir.researchlnkapi.dtos.users.UserDto;
import com.maahir.researchlnkapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //User sign up with email and password
    @PostMapping("/signup/password")
    public ResponseEntity<UserDto> registerWithPassword(@RequestBody RegisterUserByPassword request) {
        UserDto user = userService.registerUserByEmailAndPassword(request);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateUser(@AuthenticationPrincipal Object principal,
                                              @RequestBody UpdateUserRequest request) {
        UserDto updatedUser = userService.updateUser(principal, request);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getUser(@AuthenticationPrincipal Object principal) {
        UserDto userDto = userService.getUser(principal);
        return ResponseEntity.ok(userDto);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal Object principal){
        userService.deleteUser(principal);
        return ResponseEntity.noContent().build();
    }

}
