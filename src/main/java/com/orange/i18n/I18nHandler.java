package com.orange.i18n;

import io.vertx.core.Handler;
import io.vertx.ext.web.LanguageHeader;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

/**
 * Created by mohamed_waleed on 07/12/17.
 */
public class I18nHandler {

    private I18nHandler(){

    }

    public static Handler<RoutingContext> create() {
        return routingContext -> {
            List<LanguageHeader> languageHeaders = routingContext.acceptableLanguages();

            String languageCode = "en";
            if(languageHeaders != null && !languageHeaders.isEmpty()) {
                languageCode = languageHeaders.get(0).tag();
            }
            routingContext.put("language", languageCode);
            routingContext.next();
        };
    }

}
