package com.skillforge.backend.exception;


public class InternalServerException extends RuntimeException{

    public String message;

    public InternalServerException() {

    }

    public InternalServerException(String message) {
        this.message = message;
    }
}
