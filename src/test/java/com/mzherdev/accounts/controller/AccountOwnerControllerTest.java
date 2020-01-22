package com.mzherdev.accounts.controller;

import com.mzherdev.accounts.model.Account;
import com.mzherdev.accounts.model.AccountOwner;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountOwnerControllerTest extends AbstractControllerTest {

    @Test
    public void testGetById() {
        HttpRequest request = HttpRequest.GET("/owners/1");
        AccountOwner owner = client.toBlocking().retrieve(request, AccountOwner.class);
        assertEquals(1, owner.getId());
        assertEquals("test1", owner.getName());
        assertEquals("test111", owner.getLastName());
    }

    @Test
    public void testGetAccounts() {
        HttpRequest request = HttpRequest.GET("/owners/3/accounts");
        List<Account> accounts = client.toBlocking().retrieve(request, Argument.of(List.class, Account.class));
        assertEquals(2, accounts.size());
    }

    @Test
    public void testGetAll() {
        HttpRequest request = HttpRequest.GET("/owners");
        List<AccountOwner> accountOwners = client.toBlocking().retrieve(request, Argument.of(List.class, AccountOwner.class));
        assertEquals(4, accountOwners.size());
    }

    @Test
    public void testCreateAccountOwner() {
        AccountOwner owner = new AccountOwner("new1", "new111");
        HttpRequest request = HttpRequest.POST("/owners", owner);
        HttpResponse response = client.toBlocking().exchange(request, AccountOwner.class);
        owner = (AccountOwner) response.getBody().get();

        assertEquals(HttpStatus.OK, response.getStatus());
        assertEquals(5, owner.getId());
        assertEquals("new1", owner.getName());
        assertEquals("new111", owner.getLastName());
    }

    @Test
    public void testCreateAccountOwnerWithEmptyName() {
        AccountOwner owner = new AccountOwner(null, "new111");
        HttpRequest request = HttpRequest.POST("/owners", owner);
        performRequestAndAssertBadStatus(request, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testCreateAccountOwnerWithEmptyLastName() {
        AccountOwner owner = new AccountOwner("new1", "");
        HttpRequest request = HttpRequest.POST("/owners", owner);
        performRequestAndAssertBadStatus(request, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testDeleteAccountOwner() {
        HttpRequest request = HttpRequest.DELETE("/owners/4");
        HttpResponse response = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.OK, response.getStatus());
    }
    @Test
    public void testDeleteAccountOwnerWithAccounts() {
        HttpRequest request = HttpRequest.DELETE("/owners/2");
        performRequestAndAssertBadStatus(request, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testDeleteNonExistingAccount() {
        HttpRequest request = HttpRequest.DELETE("/owners/200");
        performRequestAndAssertBadStatus(request, HttpStatus.NOT_FOUND);
    }
}
