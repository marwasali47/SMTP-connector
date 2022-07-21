package com.orange.auth;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;

import java.util.Base64;

/**
 * Created by mohamed_waleed on 18/10/17.
 */
public class Oauth2Provider implements AuthProvider {

    private Vertx vertx;

    public Oauth2Provider(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> handler) {

        String clientId = authInfo.getString("clientId");
        String clientSecret = authInfo.getString("clientSecret");
        String host = authInfo.getString("host");
        Integer port = authInfo.getInteger("port");
        String checkTokenUrl = authInfo.getString("checkTokenUrl");
        String token = authInfo.getString("oauthToken");

        String appCred = clientId + ":" + clientSecret;
        String clientBasicAuthHeader = new String(Base64.getEncoder().encode(appCred.getBytes()));


        WebClient client = WebClient.create(vertx);

        client
                .get(port, host, checkTokenUrl)
                .as(BodyCodec.jsonObject())
                .addQueryParam("token", token)
                .putHeader("Authorization", "Basic " + clientBasicAuthHeader)
                .send(response ->

                        handler.handle(new AsyncResult<User>() {
                            @Override
                            public User result() {
                                HttpResponse<JsonObject> result = response.result();
                                JsonObject body = result.body();
                                com.orange.auth.User user = new com.orange.auth.User();
                                user.setUsername(body.getString("user_name"));
                                user.setAuthorities(body.getJsonArray("authorities").getList());
                                user.setScopes(body.getJsonArray("scope").getList());
                                return user;
                            }

                            @Override
                            public Throwable cause() {
                                return response.cause();
                            }

                            @Override
                            public boolean succeeded() {
                                return response.succeeded() && response.result().statusCode() == HttpResponseStatus.OK.code();
                            }

                            @Override
                            public boolean failed() {
                                return response.failed();
                            }
                        }));
    }

}
