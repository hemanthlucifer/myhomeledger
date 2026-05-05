package com.myhomeledger.app.security.exception;

public class DuplicatePhoneException extends RuntimeException {

    public DuplicatePhoneException() {
        super("An account with this phone number already exists");
    }
}
