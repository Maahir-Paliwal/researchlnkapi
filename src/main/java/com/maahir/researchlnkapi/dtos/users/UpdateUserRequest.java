package com.maahir.researchlnkapi.dtos.users;


import lombok.Data;

import javax.swing.text.html.Option;
import java.util.Optional;

//frontend -> backend

@Data
public class UpdateUserRequest {
    private String email;
}
