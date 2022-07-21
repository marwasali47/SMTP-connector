package com.orange.exchange.dtos;

import lombok.Data;

/**
 * Created by mohamed_waleed on 13/12/17.
 */
@Data
public class ConnectorDTO {

    private String clientId;

    private String clientSecret;

    private String twitterApiKey;

    private String twitterApiSecret;

    private String account;

    private String languageCode;

    private String requestUrl;

    private String domain;

    private String channelId;

    private String username;
}
