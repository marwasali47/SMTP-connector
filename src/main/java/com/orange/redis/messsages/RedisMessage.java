package com.orange.redis.messsages;

import lombok.Data;
import lombok.ToString;

import java.util.Map;

/**
 * Created by mohamed_waleed on 19/11/17.
 */
@Data
@ToString
public class RedisMessage extends Message{
    private String functionName;

    private String senderTopic;

    private String response;

    private String receiverTopic;

    private Map<String, String> parameters;

    private String traceId;

    private String userToken;
}
