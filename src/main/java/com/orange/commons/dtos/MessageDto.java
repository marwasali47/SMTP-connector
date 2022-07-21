package com.orange.commons.dtos;

import java.util.List;

/**
 * Created by Mohamed Gaber on Sep, 2018
 */
public class MessageDto {

    private String messageText;
    private String subject;
    private List<EmailAddressDTO> cc;
    private List<EmailAddressDTO> to;
    private List<EmailAddressDTO> bcc;

    public MessageDto() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public List<EmailAddressDTO> getCc() {
        return cc;
    }

    public void setCc(List<EmailAddressDTO> cc) {
        this.cc = cc;
    }

    public List<EmailAddressDTO> getTo() {
        return to;
    }

    public void setTo(List<EmailAddressDTO> to) {
        this.to = to;
    }

    public List<EmailAddressDTO> getBcc() {
        return bcc;
    }

    public void setBcc(List<EmailAddressDTO> bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "messageText='" + messageText + '\'' +
                ", subject='" + subject + '\'' +
                ", cc=" + cc +
                ", to=" + to +
                ", bcc=" + bcc +
                '}';
    }
}
