package com.rempms_message_service.dto.email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailHasAttachmentDTO {
    private Integer id;
    private String originalFileName;
    private String attachmentBase64;
    private String contentType;
}
