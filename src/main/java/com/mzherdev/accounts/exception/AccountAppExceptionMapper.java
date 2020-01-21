package com.mzherdev.accounts.exception;

import org.apache.log4j.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccountAppExceptionMapper implements ExceptionMapper<AccountAppException> {

    private final Logger log = Logger.getLogger(AccountAppExceptionMapper.class);

    public AccountAppExceptionMapper() {
    }

    @Override
    public Response toResponse(AccountAppException e) {
        log.error(e.getMessage());
        return Response.status(e.getStatus() != null ? e.getStatus() : Response.Status.INTERNAL_SERVER_ERROR)
                .entity(e.getMessage())
                .type(MediaType.APPLICATION_JSON).build();
    }
}