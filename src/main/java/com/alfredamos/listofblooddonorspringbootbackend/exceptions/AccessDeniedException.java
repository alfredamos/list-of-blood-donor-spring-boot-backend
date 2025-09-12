package com.alfredamos.listofblooddonorspringbootbackend.exceptions;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String message) {
        super(message);
    }
}
