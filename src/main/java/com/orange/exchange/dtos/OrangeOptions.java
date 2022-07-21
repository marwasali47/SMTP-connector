package com.orange.exchange.dtos;

import com.orange.commons.dtos.EmailAddressDTO;
import lombok.Data;

import java.util.List;

/**
 * Created by mohamed_waleed on 24/10/17.
 */
@Data
public class OrangeOptions implements  Options{

    private long numberOfUnreadMessages;

    private int pageNumber;

    private int totalMailsNumber;

    private boolean isRead;

    private String mailId;

    private List<EmailAddressDTO> cc;

    private List<EmailAddressDTO> to;

    private List<EmailAddressDTO> bcc;

    private EmailAddressDTO accountAddress;
}
