package com.orange.verticals;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.util.Map;
import java.util.UUID;

public class LoggingHandler {

    private LoggingHandler(){

    }
    
    public static Handler<RoutingContext> create(){

        Logger logger = LogManager.getLogger(LoggingHandler.class);

        return (routingContext -> {

            String token = routingContext.request().getHeader("Authorization").split(" ")[1];
            String traceId = UUID.randomUUID().toString();

            routingContext.put("traceId", traceId);

            ThreadContext.put("trace-id", traceId);
            ThreadContext.put("user-token", token);

            logRequest(logger, routingContext);

            routingContext.next();
        });
    }

    private static void logRequest(Logger logger, RoutingContext routingContext) {
        String requestPath = routingContext.request().path();
        String requestMethod = routingContext.request().method().name();
        String requestQueryString = routingContext.request().query();
        requestQueryString = requestQueryString == null ? "" : "?"+requestQueryString;
        MultiMap requestHeaders = routingContext.request().headers();
        String body = routingContext.getBodyAsString();

        if(body != null) {
            body = body.replaceAll("\n", "");
        }

        StringBuilder headers = new StringBuilder();
        for(Map.Entry entry : requestHeaders.entries()){
            headers.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
        }

        logger.debug("{} {}{} , body -> {}", requestMethod, requestPath, requestQueryString, body);
    }

}
