package com.orange.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by mohamed_waleed on 07/12/17.
 */
@Service
public class MessageUtility {

    @Autowired
    private MessageSource messageSource;

    public String getMessage(String key, String language) {
        Locale languageLocale = Locale.ENGLISH;
        if(language.equals(Locale.FRANCE.getLanguage())) {
            languageLocale = Locale.FRANCE;
        }else if(language.equals(Locale.ENGLISH.getLanguage())) {
            languageLocale = Locale.ENGLISH;
        }
        return messageSource.getMessage(key, null, languageLocale);
    }
}
