package com.mzherdev.accounts.exception;

import javax.ws.rs.core.Response;

public class AccountAppException extends RuntimeException {
    protected Response.Status status;

    public AccountAppException(String message) {
        super(message);
    }

    public Response.Status getStatus() {
        return status;
    }
}
