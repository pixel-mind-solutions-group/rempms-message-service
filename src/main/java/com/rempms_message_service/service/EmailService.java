package com.rempms_message_service.service;

import com.rempms_message_service.dto.email.EmailRequestDTO;
import com.rempms_message_service.util.CommonResponse;
import reactor.core.publisher.Mono;

public interface EmailService {

    Mono<CommonResponse> sendEmail(EmailRequestDTO request);
}
