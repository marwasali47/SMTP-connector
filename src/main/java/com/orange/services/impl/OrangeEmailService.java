package com.orange.services.impl;

import com.orange.services.IOrangeEmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrangeEmailService implements IOrangeEmailService {

    private Logger logger = LogManager.getLogger(OrangeEmailService.class);

    @Value("${mail.smtp.host}")
    private String smtpServerHost;

    @Value("${mail.smtp.port}")
    private String smtpServerPort;


}
