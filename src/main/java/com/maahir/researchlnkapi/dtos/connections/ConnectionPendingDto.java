package com.maahir.researchlnkapi.dtos.connections;

import lombok.Data;

@Data
public class ConnectionPendingDto {
    Long senderProfileId;
    String senderName;
    String senderEmail;
    String senderProfilePic;
}
