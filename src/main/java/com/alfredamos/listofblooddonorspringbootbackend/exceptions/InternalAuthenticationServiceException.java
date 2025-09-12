package com.alfredamos.listofblooddonorspringbootbackend.exceptions;

public class InternalAuthenticationServiceException extends RuntimeException {
    public InternalAuthenticationServiceException(String message) {
        super(message);
    }
}
