package com.alfredamos.listofblooddonorspringbootbackend.exceptions;


public class PaymentException extends RuntimeException{
    public PaymentException(String message) {
        super(message);
    }
}
