package com.alfredamos.listofblooddonorspringbootbackend.exceptions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String message) {
        super(message);
    }
}
