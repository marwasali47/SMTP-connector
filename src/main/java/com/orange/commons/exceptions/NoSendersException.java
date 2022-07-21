package com.orange.commons.exceptions;

/**
 * Created by mohamed_waleed on 28/11/17.
 */
public class NoSendersException extends EndPointException {

    public NoSendersException(String msg, Integer status) {
        super(msg, status);
    }

}
