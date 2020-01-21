package com.mzherdev.accounts.exception;

import javax.ws.rs.core.Response;

public class BadRequestAppException extends AccountAppException {

    public BadRequestAppException(String message) {
        super(message);
        status = Response.Status.BAD_REQUEST;
    }
}
