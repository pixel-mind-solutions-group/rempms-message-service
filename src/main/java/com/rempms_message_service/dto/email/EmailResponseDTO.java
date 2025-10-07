package com.rempms_message_service.dto.email;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class EmailResponseDTO {
    private Boolean success;
    private LocalDateTime sentAt;
}
