package com.maahir.researchlnkapi.dtos.users;


import lombok.Data;


//user registers via ORCID sign up
@Data
public class RegisterUserByOrcid {
    private String orcidId;
    private String email;
}
