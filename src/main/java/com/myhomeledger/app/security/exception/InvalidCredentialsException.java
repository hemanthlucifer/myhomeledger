package com.myhomeledger.app.security.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid phone number or password");
    }
}
