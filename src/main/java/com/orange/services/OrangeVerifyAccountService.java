package com.orange.services;

import com.orange.commons.exceptions.EmptyFieldException;
import com.orange.commons.exceptions.EndPointException;
import com.orange.commons.exceptions.InvalidEmailException;
import com.orange.commons.utilities.EmailUtility;
import com.orange.exchange.dtos.ConnectorDTO;
import com.orange.i18n.MessageUtility;
import com.orange.utils.IMAPStoreUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.Store;

@Service
public class OrangeVerifyAccountService {

    private Logger logger = LogManager.getLogger(OrangeVerifyAccountService.class);


    @Autowired
    private IMAPStoreUtil imapStoreUtil;

    @Autowired
    private MessageUtility messageUtility;


    public boolean verifyOrangeAccount(ConnectorDTO connectorDTO) throws EndPointException {

        checkInputData(connectorDTO);
        Store imapStore = null;
        try {
             imapStore = imapStoreUtil.connectToImap(connectorDTO.getClientId(), connectorDTO.getClientSecret(), false);
             connectorDTO.setAccount(connectorDTO.getClientId());
             return  true;
        } catch (MessagingException e) {
           logger.error(e.toString());
           throw new EndPointException(messageUtility.getMessage("payload.invalid_channel_cred", connectorDTO.getLanguageCode()), HttpStatus.UNAUTHORIZED.value());
        }
    }

    private void checkInputData(ConnectorDTO connectorDTO) throws InvalidEmailException, EmptyFieldException {
        String email = connectorDTO.getClientId();
        String password = connectorDTO.getClientSecret();
        if (email == null || email.isEmpty()) {
            throw new EmptyFieldException(messageUtility.getMessage("payload.email_empty", connectorDTO.getLanguageCode()), HttpStatus.BAD_REQUEST.value());
        }
        if (password == null || password.isEmpty()) {
            throw new EmptyFieldException(messageUtility.getMessage("payload.password_empty", connectorDTO.getLanguageCode()), HttpStatus.BAD_REQUEST.value());
        }
        boolean isValidEmail = EmailUtility.validate(email);
        if (!isValidEmail) {
            throw new InvalidEmailException(messageUtility.getMessage("payload.invalid_email", connectorDTO.getLanguageCode()), HttpStatus.BAD_REQUEST.value());
        }
    }
}
