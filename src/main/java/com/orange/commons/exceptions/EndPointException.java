package com.orange.commons.exceptions;

/**
 * Created by mohamed_waleed on 28/11/17.
 */
public class EndPointException extends Exception {

    private final Integer status;

    public EndPointException(String msg, Integer status) {
        super(msg);
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }

}
