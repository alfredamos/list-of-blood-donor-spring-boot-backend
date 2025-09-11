package com.alfredamos.listofblooddonorspringbootbackend.exceptions;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException(String message) {
        super(message);
    }
}
