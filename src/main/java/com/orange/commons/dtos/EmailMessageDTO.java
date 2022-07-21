package com.orange.commons.dtos;

import java.util.List;

/**
 * Created by Mohamed Gaber on Apr, 2019
 */
public class EmailMessageDTO {

    private String body;
    private String subject;
    private List<EmailAddressDTO> to;
    private List<EmailAddressDTO> cc;
    private List<EmailAddressDTO> bcc;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<EmailAddressDTO> getTo() {
        return to;
    }

    public void setTo(List<EmailAddressDTO> to) {
        this.to = to;
    }

    public List<EmailAddressDTO> getCc() {
        return cc;
    }

    public void setCc(List<EmailAddressDTO> cc) {
        this.cc = cc;
    }

    public List<EmailAddressDTO> getBcc() {
        return bcc;
    }

    public void setBcc(List<EmailAddressDTO> bcc) {
        this.bcc = bcc;
    }

    @Override
    public String toString() {
        return "EmailMessageDTO{" +
            "body='" + body + '\'' +
            ", subject='" + subject + '\'' +
            ", to=" + to +
            ", cc=" + cc +
            ", bcc=" + bcc +
            '}';
    }
}
