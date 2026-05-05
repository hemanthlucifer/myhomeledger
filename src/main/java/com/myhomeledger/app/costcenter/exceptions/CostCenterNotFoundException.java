package com.myhomeledger.app.costcenter.exceptions;

public class CostCenterNotFoundException extends RuntimeException {

    public CostCenterNotFoundException(String message) {
        super(message);
    }
}
