package com.mzherdev.accounts.exception;

import io.micronaut.http.HttpStatus;

public class AccountAppException extends RuntimeException {
    protected HttpStatus status;

    public AccountAppException(String message) {
        super(message);
    }

    public HttpStatus getStatus() {
        return status;
    }
}
