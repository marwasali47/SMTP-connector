package com.orange.commons.exceptions;

/**
 * Created by mohamed_waleed on 28/11/17.
 */
public class InvalidEmailException extends EndPointException {
    public InvalidEmailException(String msg, Integer status) {
        super(msg, status);
    }
}
