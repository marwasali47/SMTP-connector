package com.orange.redis.subscribers;

import com.google.gson.Gson;
import com.orange.commons.dtos.*;
import com.orange.commons.exceptions.EndPointException;
import com.orange.commons.exceptions.NoSendersException;
import com.orange.commons.responses.ServiceResponse;
import com.orange.commons.service.MappingService;
import com.orange.exception.CannotRespondToMeetingException;
import com.orange.exception.CredentialsHasExpiredException;
import com.orange.exception.MeetingOutOfDateOrNoAttendeesException;
import com.orange.exception.MeetingRequestNoFoundException;
import com.orange.i18n.MessageUtility;
import com.orange.orchestration.Orchestrator;
import com.orange.exchange.dtos.ConnectorDTO;
import com.orange.redis.messsages.RedisMessage;
import com.orange.services.OrangeService;
import com.orange.services.OrangeVerifyAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by mohamed_waleed on 19/11/17.
 */
@Service
public class RedisMessageSubscriber implements MessageListener {

    private Logger logger = LogManager.getLogger(RedisMessageSubscriber.class);

    @Autowired
    private Orchestrator orchestrator;

    @Autowired
    private MappingService mappingService;

    @Autowired
    private MessageUtility messageUtility;

    @Autowired
    private OrangeVerifyAccountService orangeVerifyAccountService;

    @Autowired
    private OrangeService orangeService;


    public void onMessage(Message message, byte[] pattern) {
        try {
            RedisMessage parsedMessage = new Gson().fromJson(new String(message.getBody(), "UTF-8"), RedisMessage.class);
            Map<String, String> parameters = parsedMessage.getParameters();

            String sender = parsedMessage.getSenderTopic();

            ThreadContext.put("trace-id", parsedMessage.getTraceId());
            ThreadContext.put("user-token", parsedMessage.getUserToken());
            logger.debug("Receive redis message on topic {} to sender  -> {}", parsedMessage.getReceiverTopic(), parsedMessage.getSenderTopic());

            switch (parsedMessage.getFunctionName()) {
                case "verifyAccount":
                    verifyOrangeAccount(parameters, sender);
                    break;

              case "composeNewMessage":
                    composeNewMessage(parameters, sender);
                    break;

             case "replyToMessage":
                    replyToMessage(parameters, sender);
                    break;
                case "getMessageAttachment":
                    getMessageAttachment(parameters, sender);
                    break;
            }
        } catch (UnsupportedEncodingException e) {
            logger.error(e);
        }
    }

    private void verifyOrangeAccount(Map<String, String> parameters, String sender) {
        ConnectorDTO connectorDTO = new Gson().fromJson(parameters.get("connectorDTO"), ConnectorDTO.class);


        ServiceResponse response = new ServiceResponse();

        RedisMessage redisMessage = new RedisMessage();
        redisMessage.setSenderTopic("orange_connector_topic");
        redisMessage.setFunctionName("verifyAccount-response");
        redisMessage.setParameters(parameters);
        redisMessage.setReceiverTopic(sender);

        try {
            boolean isVerified =orangeVerifyAccountService.verifyOrangeAccount(connectorDTO);
            if(isVerified) {
                response.setStatus(HttpStatus.OK.value());
                response.setMessage("success");
                redisMessage.getParameters().put("connectorDTO", new Gson().toJson(connectorDTO));
            }else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setMessage(messageUtility.getMessage("payload.invalid_channel_cred", connectorDTO.getLanguageCode()));
            }
            redisMessage.setResponse(new Gson().toJson(response));
            orchestrator.call(redisMessage);
        } catch (EndPointException e) {
            logger.error(e);
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage(e.getMessage());
            redisMessage.setResponse(new Gson().toJson(response));
            orchestrator.call(redisMessage);
        }
    }

    private void composeNewMessage(Map<String, String> parameters, String sender) {

        MessageRequestDto messageRequestDto = new Gson().fromJson(parameters.get("messageRequest"), MessageRequestDto.class);
        ResponseDto responseDto = new ResponseDto();
        try {
            orangeService.composeNewMessage(messageRequestDto);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setClientMessage(messageUtility.getMessage("new_message_sent", messageRequestDto.getLanguageCode()));
        } catch (NoSendersException e) {
            logger.error(e);
            responseDto.setStatusCode(HttpStatus.BAD_REQUEST.value());
            responseDto.setClientMessage(messageUtility.getMessage("no_sender_error", messageRequestDto.getLanguageCode()));
            responseDto.setDeveloperMessage(e.getMessage());
        } catch (CredentialsHasExpiredException e) {
            logger.error(e);
            responseDto.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            responseDto.setClientMessage(messageUtility.getMessage("not_authorized", messageRequestDto.getLanguageCode()));
            responseDto.setDeveloperMessage(e.getMessage());
        } catch (Exception e) {
            logger.error(e);
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setClientMessage(messageUtility.getMessage("new_message_failed", messageRequestDto.getLanguageCode()));
            responseDto.setDeveloperMessage(e.getMessage());
        }

        RedisMessage redisMessage = new RedisMessage();
        redisMessage.setSenderTopic("orange_connector_topic");
        redisMessage.setFunctionName("composeNewMessage-response");
        redisMessage.setParameters(parameters);
        redisMessage.setReceiverTopic(sender);
        redisMessage.setResponse(new Gson().toJson(responseDto));
        orchestrator.call(redisMessage);

    }

    private void replyToMessage(Map<String, String> parameters, String sender) {
        MessageRequestDto messageRequestDto = new Gson().fromJson(parameters.get("messageRequest"), MessageRequestDto.class);
        ResponseDto responseDto = new ResponseDto();
        try {
            orangeService.replyToMessage(messageRequestDto);
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setClientMessage(messageUtility.getMessage("message_reply_sent", messageRequestDto.getLanguageCode()));
        } catch (CredentialsHasExpiredException e) {
            logger.error(e);
            responseDto.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            responseDto.setClientMessage(messageUtility.getMessage("not_authorized", messageRequestDto.getLanguageCode()));
            responseDto.setDeveloperMessage(e.getMessage());
        } catch (Exception e) {
            logger.error(e);
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setClientMessage(messageUtility.getMessage("message_reply_failed", messageRequestDto.getLanguageCode()));
            responseDto.setDeveloperMessage(e.getMessage());
        }

        RedisMessage redisMessage = new RedisMessage();
        redisMessage.setSenderTopic("orange_connector_topic");
        redisMessage.setFunctionName("replyToMessage-response");
        redisMessage.setParameters(parameters);
        redisMessage.setReceiverTopic(sender);
        redisMessage.setResponse(new Gson().toJson(responseDto));
        orchestrator.call(redisMessage);
    }

    private void getMessageAttachment(Map<String, String> parameters, String sender) {

        MessageAttachmentRequestDto messageRequestDto = new Gson().fromJson(parameters.get("messageAttachmentRequest"), MessageAttachmentRequestDto.class);
        MessageAttachmentResponseDto responseDto = new MessageAttachmentResponseDto();
        try {
            responseDto.setContent(orangeService.getMessageAttachment(messageRequestDto));
            responseDto.setStatusCode(HttpStatus.OK.value());
            responseDto.setClientMessage(messageUtility.getMessage("message_attachment_sent", messageRequestDto.getLanguageCode()));
        } catch (CredentialsHasExpiredException e) {
            logger.error(e);
            responseDto.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            responseDto.setClientMessage(messageUtility.getMessage("not_authorized", messageRequestDto.getLanguageCode()));
            responseDto.setDeveloperMessage(e.getMessage());
        } catch (Exception e) {
            logger.error(e);
            responseDto.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            responseDto.setClientMessage(messageUtility.getMessage("message_attachment_failed", messageRequestDto.getLanguageCode()));
            responseDto.setDeveloperMessage(e.getMessage());
        }

        RedisMessage redisMessage = new RedisMessage();
        redisMessage.setSenderTopic("orange_connector_topic");
        redisMessage.setFunctionName("getMessageAttachment-response");
        redisMessage.setParameters(parameters);
        redisMessage.setReceiverTopic(sender);
        redisMessage.setResponse(new Gson().toJson(responseDto));
        orchestrator.call(redisMessage);
    }
}