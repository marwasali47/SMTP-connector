package com.orange.services;


import com.orange.commons.dtos.MessageAttachmentRequestDto;
import com.orange.commons.dtos.MessageRequestDto;
import com.orange.commons.exceptions.NoSendersException;

import javax.mail.MessagingException;
import java.io.IOException;

public interface OrangeService {

    void composeNewMessage(MessageRequestDto messageRequestDto) throws NoSendersException, MessagingException, IOException;

    void replyToMessage(MessageRequestDto messageRequestDto) throws MessagingException, IOException;

    byte[] getMessageAttachment(MessageAttachmentRequestDto messageRequestDto);
}
