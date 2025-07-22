package com.maahir.researchlnkapi.dtos.users;


import lombok.Data;

//user login with email and password
@Data
public class LoginUserRequest {
    private String email;
    private String password;
}
