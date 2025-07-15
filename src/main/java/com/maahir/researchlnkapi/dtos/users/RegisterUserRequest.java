package com.maahir.researchlnkapi.dtos.users;

import lombok.Data;

//manual sign in: frontend -> backend
@Data
public class RegisterUserRequest {
    private String email;
    private String password;
}
