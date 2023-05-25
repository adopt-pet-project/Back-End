package com.adoptpet.server.commons.exception;

public class JwtException extends RuntimeException{

    public JwtException(String message) {
        super(message);
    }
}
