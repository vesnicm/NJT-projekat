package com.fon.prijavaBackend.exception;

//409
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
