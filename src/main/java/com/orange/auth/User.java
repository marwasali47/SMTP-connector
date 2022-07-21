package com.orange.auth;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;

import java.util.List;

/**
 * Created by mohamed_waleed on 19/10/17.
 */
public class User implements io.vertx.ext.auth.User {

    private String username;

    private List<String> authorities;

    private List<String> scopes;

    @Override
    public io.vertx.ext.auth.User isAuthorised(String s, Handler<AsyncResult<Boolean>> handler) {
        return null;
    }

    @Override
    public io.vertx.ext.auth.User clearCache() {
        return null;
    }

    @Override
    public JsonObject principal() {
        JsonObject principal = new JsonObject();
        principal.put("username", username);
        principal.put("authorities", authorities);
        principal.put("scopes", scopes);
        return principal;
    }

    @Override
    public void setAuthProvider(AuthProvider authProvider) {
        /// Do nothing just from parent
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }


    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }
}
