package com.orange.commons.dtos;

/**
 * Created by Mohamed Gaber on Apr, 2019
 */
public class MeetingActionMessageDto {

    private CommonMessageDto message;
    private UserChannelDTO userChannel;
    private String languageCode;
    private MeetingActionRequestDTO meetingActionRequestDTO;

    public MeetingActionMessageDto() {
    }

    public MeetingActionMessageDto(CommonMessageDto message, UserChannelDTO userChannel, String languageCode, MeetingActionRequestDTO meetingActionRequestDTO) {
        this.message = message;
        this.userChannel = userChannel;
        this.languageCode = languageCode;
        this.meetingActionRequestDTO = meetingActionRequestDTO;
    }

    public CommonMessageDto getMessage() {
        return message;
    }

    public void setMessage(CommonMessageDto message) {
        this.message = message;
    }

    public UserChannelDTO getUserChannel() {
        return userChannel;
    }

    public void setUserChannel(UserChannelDTO userChannel) {
        this.userChannel = userChannel;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public MeetingActionRequestDTO getMeetingActionRequestDTO() {
        return meetingActionRequestDTO;
    }

    public void setMeetingActionRequestDTO(MeetingActionRequestDTO meetingActionRequestDTO) {
        this.meetingActionRequestDTO = meetingActionRequestDTO;
    }

    @Override
    public String toString() {
        return "MeetingActionMessageDto{" +
            "message=" + message +
            ", userChannel=" + userChannel +
            ", languageCode='" + languageCode + '\'' +
            ", meetingActionRequestDTO=" + meetingActionRequestDTO +
            '}';
    }
}
