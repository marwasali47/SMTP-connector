package com.orange.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Created by mohamed_waleed on 07/12/17.
 */
@Configuration
public class I18nConfig {
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasenames("i18n/messages");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }
}
