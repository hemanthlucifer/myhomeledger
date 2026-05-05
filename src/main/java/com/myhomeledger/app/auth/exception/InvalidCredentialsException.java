package com.myhomeledger.app.auth.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("Invalid phone number or password");
    }
}
