package com.orange.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.Session;
import java.util.Properties;

/**
 * Created by Mohamed Gaber on Oct, 2018
 */

@Configuration
public class EmailConfig {

    @Value("${mail.smtp.port}")
    private Integer smtpServerPort;

    @Value("${proxy.host}")
    private String proxyHost;

    @Value("${proxy.port}")
    private Integer proxyPort;

    @Value("${proxy.enabled}")
    private boolean isExchangeProxyEnabled;

    @Bean
    public Session session(){
        Properties props = System.getProperties();
        if(isExchangeProxyEnabled){
            props.put(String.format("mail.%s.proxy.host", "smtp"), proxyHost);
            props.put(String.format("mail.%s.proxy.port", "smtp"), proxyPort);
        }
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", smtpServerPort);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.port", smtpServerPort);
        return Session.getDefaultInstance(props);
    }

}
