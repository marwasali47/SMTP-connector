package com.orange.commons.dtos;

import lombok.Data;

/**
 * Created by mohamed_waleed on 06/11/17.
 */
@Data
public class UserChannelDTO {

    private int id;

    private String username;

    private int channelId;

    private String channelUsername;

    private String channelPassword;

    private String twitterApiKey;

    private String twitterApiSecret;

    private String accountName;

    private ChannelDTO channel;

}
