package com.orange.services.impl;

import com.orange.commons.dtos.*;
import com.orange.commons.exceptions.NoSendersException;
import com.orange.exception.CredentialsHasExpiredException;
import com.orange.exception.GetMessageAttachmentFailedException;
import com.orange.orchestration.Orchestrator;
import com.orange.services.IOrangeEmailService;
import com.orange.services.OrangeService;
import com.orange.utils.IMAPStoreUtil;
import com.sun.mail.smtp.SMTPTransport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.search.MessageIDTerm;
import javax.mail.search.SearchTerm;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class OrangeServiceImpl implements OrangeService {

    private Logger logger = LogManager.getLogger(OrangeServiceImpl.class);

    @Autowired
    private IMAPStoreUtil imapStoreUtil;

    @Autowired
    private Orchestrator orchestrator;

    @Autowired
    private IOrangeEmailService orangeEmailService;

    @Autowired
    private Session session;

    @Value("${mail.smtp.host}")
    private String smtpServerHost;

    @Override
    public void composeNewMessage(MessageRequestDto messageRequestDto) throws NoSendersException, MessagingException, IOException {

        MessageDto messageDto = messageRequestDto.getMessageDto();
        UserChannelDTO userChannel = messageRequestDto.getUserChannel();
        String userEmail = userChannel.getChannelUsername();

        if(isEmptyList(messageDto.getTo()) && isEmptyList(messageDto.getCc()) && isEmptyList(messageDto.getBcc())) {
            throw new NoSendersException("Message must has at least one recipient", 400);
        }

        MimeMessage message = new MimeMessage(session);
        message.setFrom(userEmail);
        message.setSubject(messageDto.getSubject());

        if(messageDto.getMessageText() != null) {
            message.setText(reformatTextNewLines(messageDto.getMessageText()), "UTF-8", "html");
        } else {
            message.setText("", "UTF-8", "html");
        }

        setMessageRecipients(messageRequestDto, message);

        sendMail(userChannel, message);

    }

    @Override
    public void replyToMessage(MessageRequestDto messageRequestDto) throws MessagingException, IOException {

        UserChannelDTO userChannel = messageRequestDto.getUserChannel();
        String messageContent = constructReplyMessage(messageRequestDto.getMessage(), messageRequestDto.getMessageDto());
        MimeMessage message = new MimeMessage(session);
        message.setFrom(userChannel.getChannelUsername());
        setEmailSubject(messageRequestDto, message);
        setMessageRecipients(messageRequestDto, message);
        message.setText(messageContent, "UTF-8", "html");
        sendMail(userChannel, message);
    }

    @Override
    public byte[] getMessageAttachment(MessageAttachmentRequestDto messageRequestDto) {

        try {
            Store imapStore = imapStoreUtil.connectToImap(messageRequestDto.getUserChannel().getChannelUsername()
                    , messageRequestDto.getUserChannel().getChannelPassword(), false);
            Folder inbox= imapStore.getFolder("inbox");
            inbox.open(Folder.READ_ONLY);
            Message message = inbox.getMessage(new Integer(messageRequestDto.getMessageId()));
            if(message != null){
                String contentType = message.getContentType();
                if (contentType.contains("multipart")) {
                    Multipart multiPart = (Multipart) message.getContent();
                    int numberOfParts = multiPart.getCount();
                    int attachmentIndex = 0;
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            String fileName = part.getFileName();
                            if(fileName.equals(messageRequestDto.getAttachmentId().split(":")[0])
                                  &&  attachmentIndex == Integer.valueOf(messageRequestDto.getAttachmentId().split(":")[1])){
                                byte[] byteArray = new byte[part.getSize()];
                                part.getInputStream().read(byteArray);
                                return byteArray;
                            }
                            attachmentIndex++;
                        }

                    }
                }
            }else{
                throw new GetMessageAttachmentFailedException();
            }
        } catch (MessagingException e) {
            if(e instanceof  AuthenticationFailedException){
                throw new CredentialsHasExpiredException();
            }
            throw new GetMessageAttachmentFailedException();
        }catch (IOException e){
            throw new GetMessageAttachmentFailedException();
        }
        throw new GetMessageAttachmentFailedException();
    }

    private void setEmailSubject(MessageRequestDto messageRequestDto, MimeMessage message) throws MessagingException {
        String subject = messageRequestDto.getMessage().getTitle();

        if (subject == null && subject.length() < 3) {
            subject = "RE: ";
        } else if (subject.substring(0, 3).toLowerCase().equals("fw:")) {
            subject = subject.replaceFirst(subject.substring(0, 3), "RE:");
        } else if(!subject.substring(0, 3).toLowerCase().equals("re:")) {
            subject = "RE: " + subject;
        }

        message.setSubject(subject);
    }

    private String constructReplyMessage(CommonMessageDto message, MessageDto messageDto) {

        String oldMessageHeader = constructMessageHeader(message, messageDto);
        StringBuilder replyMessage = new StringBuilder();
        replyMessage.append(reformatTextNewLines(messageDto.getMessageText()));
        replyMessage.append(oldMessageHeader);
        String oldMessage = Jsoup.parse(message.getDesc()).body().toString();
        replyMessage.append(oldMessage);
        return replyMessage.toString();
    }

    private String constructMessageHeader(CommonMessageDto message, MessageDto messageDto) {

        String subjectStr = message.getTitle() != null ? message.getTitle()  : "";
        String sentDate = new Date(message.getDate()).toString();
        StringBuilder header = new StringBuilder();
        header.append("<p class=\"MsoNormal\"><span style=\"color:black\"><o:p>&nbsp;</o:p></span></p>");
        header.append("<div style=\"border-top: 1pt solid rgb(181, 196, 223);border-right: none;border-bottom: none;border-left: none;border-image: initial;padding: 3pt 0in 0in;\">");
        header.append("<p class=\"MsoNormal\">");
        header.append("<span style=\"font-size: 10pt; font-family: Tahoma, sans-serif;\">");
        header.append("<b>From: </b>").append(message.getFrom()).append("<br/>");
        header.append("<b>Sent: </b>").append(sentDate).append("<br/>");
        header.append("<b>To: </b>").append(getCcRecipientsAsString(messageDto.getTo())).append("<br/>");
        if (messageDto.getCc() != null) {
            header.append("<b>Cc: </b>").append(getCcRecipientsAsString(messageDto.getCc())).append("<br/>");
        }
        header.append("<b>Subject: </b>").append(subjectStr);
        header.append("<o:p></o:p>");
        header.append("</span>");
        header.append("</p>");
        header.append("</div>");
        return header.toString();
    }

    private String getCcRecipientsAsString(List<EmailAddressDTO> recipients) {

        if (recipients != null && !recipients.isEmpty()) {
            StringBuilder recipientstr = new StringBuilder();
            for (int i = 0; i < recipients.size(); i++) {
                recipientstr.append(recipients.get(i).getName());
                if (i < recipients.size() - 1) {
                    recipientstr.append("; ");
                }
            }
            return recipientstr.toString();
        } else {
            return null;
        }
    }

    private void sendMail(UserChannelDTO userChannel, Message message) throws MessagingException, IOException {

        SMTPTransport transport = new SMTPTransport(session, null);
        try {
            transport.connect(""+ smtpServerHost, userChannel.getChannelUsername(), userChannel.getChannelPassword());
            transport.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException ex) {
            logger.error(ex);
            if(ex instanceof AuthenticationFailedException){
                throw new CredentialsHasExpiredException();
            }
            throw ex;
        }
    }

    private <T> boolean isEmptyList(List<T> list){
        return list == null || list.isEmpty();
    }

    private String reformatTextNewLines(String messageText) {
        return messageText.replace("\n", "<br/>");
    }

    private void setMessageRecipients(MessageRequestDto messageRequestDto, MimeMessage message) throws MessagingException {
        List<EmailAddressDTO> toList = messageRequestDto.getMessageDto().getTo();
        List<EmailAddressDTO> ccList = messageRequestDto.getMessageDto().getCc();
        List<EmailAddressDTO> bccList = messageRequestDto.getMessageDto().getBcc();

        if(toList != null && !toList.isEmpty()){
            Address[] toEmailsAddresses = toAddressArray(toList);
            message.setRecipients(Message.RecipientType.TO, toEmailsAddresses);
        }
        if(ccList != null && !ccList.isEmpty()){
            Address[] ccEmailsAddresses = toAddressArray(ccList);
            message.setRecipients(Message.RecipientType.CC, ccEmailsAddresses);
        }
        if(bccList != null && !bccList.isEmpty()){
            Address[] bccEmailsAddresses = toAddressArray(bccList);
            message.setRecipients(Message.RecipientType.BCC, bccEmailsAddresses);
        }
    }

    private Address[] toAddressArray(List<EmailAddressDTO> addressDTOList) {
        return addressDTOList.stream()
                .map(this::toEmailAddress)
                .toArray(Address[]::new);
    }

    private String extractEmail(String emailStr){
        String toMail="";
        if(emailStr.contains("<")){
            Pattern pattern = Pattern.compile(".*<(.*)>.*");
            Matcher matcher = pattern.matcher(emailStr);
            matcher.find();
            toMail = matcher.group(1);
        }else{
            toMail=emailStr;
        }
        return toMail;
    }

    private Address toEmailAddress(EmailAddressDTO emailAddressDTO){
        String email = extractEmail(emailAddressDTO.getEmail());
        try {
            return InternetAddress.parse(email)[0];
        } catch (AddressException e) {
            logger.error(e);
        }
        return null;
    }
}
