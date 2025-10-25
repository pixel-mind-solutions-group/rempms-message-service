package com.rempms_message_service.service.impl;

import com.rempms_message_service.dto.email.EmailRequestDTO;
import com.rempms_message_service.dto.email.EmailResponseDTO;
import com.rempms_message_service.service.EmailService;
import com.rempms_message_service.util.CommonResponse;
import com.rempms_message_service.util.ExceptionExtractor;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    /**
     * Sends an email using Spring's {@link JavaMailSender} in a non-blocking reactive manner.
     *
     * <p>This method wraps the traditional blocking email-sending logic (using SMTP) inside
     * {@link Schedulers#boundedElastic()} to avoid blocking the main WebFlux event loop.
     * The boundedElastic scheduler is designed for short-lived blocking I/O tasks such as
     * sending emails, performing file operations, or accessing databases through legacy drivers.</p>
     *
     * <p>The method performs the following steps:</p>
     * <ul>
     *     <li>Creates a {@link MimeMessage} and populates it with details from {@link EmailRequestDTO}
     *         — subject, body, recipients (To, CC, BCC), and formatting options.</li>
     *     <li>Uses {@link MimeMessageHelper} to handle multipart messages and character encoding.</li>
     *     <li>Sends the email through the configured {@link JavaMailSender} instance.</li>
     *     <li>Emits a {@link CommonResponse} containing an {@link EmailResponseDTO} with success or failure information.</li>
     *     <li>In case of any exception (SMTP/network/configuration errors), captures the root cause message
     *         and emits a {@link CommonResponse} with an error status instead of throwing.</li>
     * </ul>
     *
     * <p>This ensures the reactive pipeline remains non-blocking and responsive, while delegating
     * the blocking SMTP I/O work to an appropriate background thread.</p>
     *
     * @param request {@link EmailRequestDTO} containing subject, body, recipients, and formatting details.
     * @return {@link Mono}&lt;{@link CommonResponse}&gt; - a reactive Mono emitting the success or failure response of the email operation.
     * @author maleeshasa
     * @see org.springframework.mail.javamail.JavaMailSender
     * @see org.springframework.mail.javamail.MimeMessageHelper
     * @see reactor.core.scheduler.Schedulers#boundedElastic()
     */
    @Override
    public Mono<CommonResponse> sendEmail(EmailRequestDTO request) {

        /*
        Mono.fromCallable(() -> { *//* blocking code *//* })
                .subscribeOn(Schedulers.boundedElastic());
        */
        //“Run this blocking call on a dedicated elastic thread pool.”
        //The boundedElastic scheduler is designed specifically for short-lived blocking I/O like database, file, or SMTP calls.
        //It uses a pool of threads that expand up to a limit (by default 10× number of CPU cores).
        //So your main event loop stays non-blocked, and WebFlux remains reactive end-to-end.

        return Mono.fromCallable(() -> {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setSubject(request.getSubject());
                helper.setText(request.getBody(), request.getIsHtml());

                if (request.getToEmails() != null && !request.getToEmails().isEmpty()) {
                    helper.setTo(request.getToEmails().toArray(new String[0]));
                }
                if (request.getCcEmails() != null && !request.getCcEmails().isEmpty()) {
                    helper.setCc(request.getCcEmails().toArray(new String[0]));
                }
                if (request.getBccEmails() != null && !request.getBccEmails().isEmpty()) {
                    helper.setBcc(request.getBccEmails().toArray(new String[0]));
                }

                mailSender.send(message);
                log.info("Email sent successfully to {}", request.getToEmails());
                log.info("Email sent successfully cc {}", request.getCcEmails());
                log.info("Email sent successfully bcc {}", request.getBccEmails());

                // Emit a success response
                return new CommonResponse(HttpStatus.OK, "Email sent successfully",
                        new EmailResponseDTO(Boolean.TRUE, LocalDateTime.now())
                );

            } catch (Exception e) {
                log.error("Failed to send email: {}", e.getMessage(), e);

                String detailedMessage = ExceptionExtractor.getRootCauseMessage(e);

                // Emit an error response instead of throwing exception directly
                return new CommonResponse(HttpStatus.BAD_REQUEST, "Email sent failed: " + detailedMessage,
                        new EmailResponseDTO(Boolean.FALSE, LocalDateTime.now())
                );
            }
        }).subscribeOn(Schedulers.boundedElastic()); // Run blocking I/O (SMTP) on a separate thread, won’t block WebFlux threads
    }
}
