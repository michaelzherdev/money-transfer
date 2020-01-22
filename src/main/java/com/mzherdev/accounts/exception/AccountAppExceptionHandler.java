package com.mzherdev.accounts.exception;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;


@Produces
@Singleton
@Requires(classes = {AccountAppException.class})
public class AccountAppExceptionHandler implements ExceptionHandler<AccountAppException, HttpResponse> {

    private final Logger log = LoggerFactory.getLogger(AccountAppExceptionHandler.class);

    @Override
    public HttpResponse handle(HttpRequest request, AccountAppException e) {
        log.error(e.getMessage());
        HttpResponse response;
        if (e.getStatus() != null) {
            response = HttpResponse.badRequest(e.getMessage());
        } else {
            response = HttpResponse.serverError(e.getMessage());
        }
        return response;
    }
}