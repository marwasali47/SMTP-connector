package com.orange.commons.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageAttachmentRequestDto {

    private String messageId;
    private String attachmentId;
    private UserChannelDTO userChannel;
    private String languageCode;



    @Override
    public String toString() {
        return "MessageAttachmentRequestDto{" +
            ", messageId=" + messageId +
            ", attachmentId=" + attachmentId +
            ", userChannel=" + userChannel +
            ", languageCode='" + languageCode + '\'' +
            '}';
    }
}
