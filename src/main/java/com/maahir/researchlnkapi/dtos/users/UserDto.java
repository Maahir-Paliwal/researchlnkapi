package com.maahir.researchlnkapi.dtos.users;

import lombok.Data;
import java.time.LocalDateTime;

//backend -> frontend

@Data
public class UserDto {
    private Long id;
    private String email;
    private String orcidId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
