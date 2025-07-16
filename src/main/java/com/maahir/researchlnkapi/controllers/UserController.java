package com.maahir.researchlnkapi.controllers;

import com.maahir.researchlnkapi.dtos.users.RegisterUserRequest;
import com.maahir.researchlnkapi.dtos.users.UpdateUserRequest;
import com.maahir.researchlnkapi.dtos.users.UserDto;
import com.maahir.researchlnkapi.model.entities.User;
import com.maahir.researchlnkapi.services.UserService;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    //User sign up with username + password
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterUserRequest request){
        UserDto user = userService.registerUser(request);
        return ResponseEntity.ok(user);
    }

    //get a user by their database ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request){
        UserDto updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }



}
