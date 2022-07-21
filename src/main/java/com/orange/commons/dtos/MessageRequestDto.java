package com.orange.commons.dtos;


/**
 * Created by Mohamed Gaber on Oct, 2018
 */
public class MessageRequestDto {

    private CommonMessageDto message;
    private UserChannelDTO userChannel;
    private MessageDto messageDto;
    private String languageCode;

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

    public MessageDto getMessageDto() {
        return messageDto;
    }

    public void setMessageDto(MessageDto messageDto) {
        this.messageDto = messageDto;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    @Override
    public String toString() {
        return "ReplyRequestDto{" +
                ", message=" + message +
                ", userChannel=" + userChannel +
                ", messageReplyDto=" + messageDto +
                ", languageCode='" + languageCode + '\'' +
                '}';
    }
}
