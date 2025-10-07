package com.rempms_message_service.controller;

import com.rempms_message_service.dto.email.EmailRequestDTO;
import com.rempms_message_service.dto.email.EmailResponseDTO;
import com.rempms_message_service.util.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping(value = "/api/message/email/v1")
public class EmailController {

    @PostMapping(value = "/send")
    public ResponseEntity<CommonResponse> sendEmail(@RequestBody EmailRequestDTO request) {
        log.info("EmailController.sendEmail() => started.");
        return ResponseEntity.ok(
                new CommonResponse(
                        HttpStatus.OK,
                        "Email sent successfully",
                        new EmailResponseDTO(Boolean.TRUE, LocalDateTime.now())
                )
        );
    }
}
