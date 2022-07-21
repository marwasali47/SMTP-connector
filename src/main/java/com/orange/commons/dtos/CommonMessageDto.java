package com.orange.commons.dtos;

/**
 * Created By Mamdouh on 3/14/2018
 */

import lombok.Data;

@Data
public class CommonMessageDto {

    private String userChannelId;
    private Long date;
    private String messageId;
    private String iconUrl;
    private String from;
    private String snippet;
    private String title;
    private String desc;
    private String channel;
    private String attachmentsStr;
    private String optionsStr;
    private Boolean isRead;
    private String fromEmail;
}
