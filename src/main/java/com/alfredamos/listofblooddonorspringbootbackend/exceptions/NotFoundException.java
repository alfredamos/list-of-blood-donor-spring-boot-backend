package com.alfredamos.listofblooddonorspringbootbackend.exceptions;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
