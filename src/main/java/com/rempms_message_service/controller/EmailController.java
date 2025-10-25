package com.rempms_message_service.controller;

import com.rempms_message_service.dto.email.EmailRequestDTO;
import com.rempms_message_service.service.EmailService;
import com.rempms_message_service.util.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author maleeshasa
 * @since 2025/10/06
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/message/email/v1")
public class EmailController {

    private final EmailService emailService;

    @PostMapping(value = "/send")
    public Mono<ResponseEntity<CommonResponse>> sendEmail(@RequestBody EmailRequestDTO request) {
        log.info("EmailController.sendEmail() => started.");
        return emailService.sendEmail(request)
                .map(response -> ResponseEntity.ok(response));
    }
}
