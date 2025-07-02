package com.maahir.researchlnkapi.dtos.users;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String orcidId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
