package com.orange.commons.responses;

/**
 * Created by mohamed_waleed on 19/10/17.
 */
public class AbstractResponse {

    private String message;

    public AbstractResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
