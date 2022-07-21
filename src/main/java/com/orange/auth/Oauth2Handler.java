package com.orange.auth;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;


/**
 * Created by mohamed_waleed on 19/10/17.
 */
public class Oauth2Handler {

    private Oauth2Handler(){

    }

    public static Handler<RoutingContext> create(AuthInfo authInfo, Oauth2Provider oauth2Provider) {

        return (routingContext -> {

            HttpServerResponse response = routingContext.response();
            String authHeader = routingContext.request().getHeader("Authorization");


            if(authHeader == null) {
                response.setStatusCode(HttpResponseStatus.UNAUTHORIZED.code()).end("auth");
                return;
            }
            String[] authHeaderParts = authHeader.split(" ");
            if(authHeaderParts.length != 2) {
                response.setStatusCode(HttpResponseStatus.UNAUTHORIZED.code()).end("Token is required");
                return;
            }
            String token = authHeaderParts[1];
            if(token == null) {
                response.setStatusCode(HttpResponseStatus.UNAUTHORIZED.code()).end("Token is required");
                return;
            }

            JsonObject authObject = new JsonObject();
            authObject.put("clientId", authInfo.getClientId());
            authObject.put("clientSecret", authInfo.getClientSecret());
            authObject.put("checkTokenUrl", authInfo.getCheckTokenUrl());
            authObject.put("host", authInfo.getHost());
            authObject.put("port", authInfo.getPort());
            authObject.put("oauthToken", token);

            oauth2Provider.authenticate(authObject, userAsyncResult -> {
                if(userAsyncResult.succeeded()){
                    routingContext.setUser(userAsyncResult.result());
                    routingContext.next();
                }else {
                   response.setStatusCode(HttpResponseStatus.UNAUTHORIZED.code()).end();
                }
            });

        });
    }
}
