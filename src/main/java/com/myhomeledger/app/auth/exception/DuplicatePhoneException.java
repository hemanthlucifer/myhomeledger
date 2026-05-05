package com.myhomeledger.app.auth.exception;

public class DuplicatePhoneException extends RuntimeException {

    public DuplicatePhoneException() {
        super("An account with this phone number already exists");
    }
}
