package com.mzherdev.accounts.controller;

import com.mzherdev.accounts.AccountApplication;
import com.mzherdev.accounts.model.Account;
import io.micronaut.context.ApplicationContext;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public abstract class AbstractControllerTest {

    private static EmbeddedServer server;
    protected static HttpClient client;

    @BeforeAll
    public static void setupServer() {
        server = ApplicationContext.build(AccountApplication.class).run(EmbeddedServer.class);
        client = server.getApplicationContext().createBean(HttpClient.class, server.getURL());
    }

    @AfterAll
    public static void stopServer() {
        if (server != null) {
            server.stop();
        }
        if (client != null) {
            client.stop();
        }
    }

    protected void performRequestAndAssertBadStatus(HttpRequest request, HttpStatus status, String message) {
        HttpClientResponseException httpClientResponseException = assertThrows(HttpClientResponseException.class, () ->
                client.toBlocking().exchange(request, Account.class));
        assertEquals(status, httpClientResponseException.getResponse().status());
        assertEquals(message, httpClientResponseException.getMessage());
    }

}
