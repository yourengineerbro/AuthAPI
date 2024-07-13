package com.yourengineerbro.AuthAPI.exception;


public class JWTVerificationException extends RuntimeException {

    public JWTVerificationException() {
        super();
    }

    public JWTVerificationException(String message) {
        super(message);
    }

    public JWTVerificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JWTVerificationException(Throwable cause) {
        super(cause);
    }
}
