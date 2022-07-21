package com.orange.commons.dtos;

import com.orange.commons.enums.MeetingResponseAction;
import com.orange.commons.enums.MeetingResponseSubAction;

/**
 * Created by Mohamed Gaber on Apr, 2019
 */
public class MeetingActionRequestDTO{

    private MeetingResponseAction action;
    private MeetingResponseSubAction subAction;
    private EmailMessageDTO customResponse;

    public MeetingResponseAction getAction() {
        return action;
    }

    public void setAction(MeetingResponseAction action) {
        this.action = action;
    }

    public MeetingResponseSubAction getSubAction() {
        return subAction;
    }

    public void setSubAction(MeetingResponseSubAction subAction) {
        this.subAction = subAction;
    }

    public EmailMessageDTO getCustomResponse() {
        return customResponse;
    }

    public void setCustomResponse(EmailMessageDTO customResponse) {
        this.customResponse = customResponse;
    }

    @Override
    public String toString() {
        return "MeetingActionRequestDTO{" +
            "action=" + action +
            ", subAction=" + subAction +
            ", customResponse=" + customResponse +
            '}';
    }
}
