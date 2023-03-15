package com.noctus.spring_security.exception;

public class InvalidJwtTokenException extends Throwable {
    public InvalidJwtTokenException(String message) {
        super(message);
    }
}
