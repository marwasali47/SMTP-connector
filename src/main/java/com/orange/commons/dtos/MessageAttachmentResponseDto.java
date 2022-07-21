package com.orange.commons.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;



@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageAttachmentResponseDto {
    private Integer statusCode;
    private String clientMessage;
    private String developerMessage;
    private byte[] content;
    private String name;
}
