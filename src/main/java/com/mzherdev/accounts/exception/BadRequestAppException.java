package com.mzherdev.accounts.exception;

import io.micronaut.http.HttpStatus;

public class BadRequestAppException extends AccountAppException {

    public BadRequestAppException(String message) {
        super(message);
        status = HttpStatus.BAD_REQUEST;
    }
}
