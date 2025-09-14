package com.alfredamos.listofblooddonorspringbootbackend.exceptions;

public class JwtException extends RuntimeException{
    public JwtException(String message) {
        super(message);
    }
}
