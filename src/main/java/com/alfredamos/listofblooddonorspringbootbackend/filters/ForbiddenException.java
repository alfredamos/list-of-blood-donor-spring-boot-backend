package com.alfredamos.listofblooddonorspringbootbackend.filters;


public class ForbiddenException extends RuntimeException{
    public ForbiddenException(String message) {
        super(message);
    }
}
