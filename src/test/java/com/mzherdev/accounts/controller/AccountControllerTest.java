package com.mzherdev.accounts.controller;

import com.mzherdev.accounts.model.Account;
import com.mzherdev.accounts.model.Currency;
import com.mzherdev.accounts.model.dto.AccountTransfer;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountControllerTest extends AbstractControllerTest {

    @Test
    public void testGetById() {
        HttpRequest request = HttpRequest.GET("/accounts/1");
        Account account = client.toBlocking().retrieve(request, Account.class);
        assertEquals(1, account.getOwnerId());
        assertEquals(1, account.getId());
    }

    @Test
    public void testGetAll() {
        HttpRequest request = HttpRequest.GET("/accounts");
        List<Account> accounts = client.toBlocking().retrieve(request, Argument.of(List.class, Account.class));
        assertEquals(4, accounts.size());
    }

    @Test
    public void testCreateAccount() {
        Account account = new Account(BigDecimal.TEN, "USD", 1);
        HttpRequest request = HttpRequest.POST("/accounts", account);
        HttpResponse response = client.toBlocking().exchange(request, Account.class);
        account = (Account) response.getBody().get();

        assertEquals(5, account.getId());
        assertEquals(1, account.getOwnerId());
        assertEquals(Currency.USD.name(), account.getCurrencyCode());
    }

    @Test
    public void testCreateAccountWithUnExistedOwner() {
        Account account = new Account(BigDecimal.TEN, "USD", 100);
        HttpRequest request = HttpRequest.POST("/accounts", account);
        performRequestAndAssertBadStatus(request, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testCreateAccountWithNegativeBalance() {
        Account account = new Account(BigDecimal.ONE.negate(), "USD", 1);
        HttpRequest request = HttpRequest.POST("/accounts", account);
        performRequestAndAssertBadStatus(request, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testCreateAccountWithUnexistedCurrencyBalance() {
        Account account = new Account(BigDecimal.ONE, "RUS", 1);
        HttpRequest request = HttpRequest.POST("/accounts", account);
        performRequestAndAssertBadStatus(request, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testDeleteAccount() {
        HttpRequest request = HttpRequest.DELETE("/accounts/2");
        HttpResponse response = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    public void testDeleteNonExistingAccount() {
        performRequestAndAssertBadStatus(HttpRequest.DELETE("/accounts/200"), HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDepositSuccessful() {
        HttpRequest request = HttpRequest.GET("/accounts/3");
        Account account = client.toBlocking().retrieve(request, Account.class);
        assertEquals(3, account.getOwnerId());
        assertEquals(new BigDecimal("300.00"), account.getAmount());

        request = HttpRequest.PUT("/accounts/3/deposit/10", account);
        account = client.toBlocking().retrieve(request, Account.class);
        assertEquals(3, account.getOwnerId());
        assertEquals(new BigDecimal("310.00"), account.getAmount());
    }

    @Test
    public void testDepositWithZeroAmount() {
        HttpRequest request = HttpRequest.GET("/accounts/1");
        Account account = client.toBlocking().retrieve(request, Account.class);

        HttpRequest putRequest = HttpRequest.PUT("/accounts/1/deposit/0", account);
        performRequestAndAssertBadStatus(putRequest, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testDepositNotExistedAccount() {
        HttpRequest request = HttpRequest.GET("/accounts/1");
        Account account = client.toBlocking().retrieve(request, Account.class);

        HttpRequest putRequest = HttpRequest.PUT("/accounts/100/deposit/10", account);
        performRequestAndAssertBadStatus(putRequest, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWithdrawSuccessful() {
        HttpRequest request = HttpRequest.GET("/accounts/1");
        Account account = client.toBlocking().retrieve(request, Account.class);
        assertEquals(1, account.getOwnerId());
        assertEquals(new BigDecimal("100.00"), account.getAmount());

        request = HttpRequest.PUT("/accounts/1/withdraw/10", account);
        account = client.toBlocking().retrieve(request, Account.class);
        assertEquals(1, account.getOwnerId());
        assertEquals(new BigDecimal("90.00"), account.getAmount());
    }

    @Test
    public void testWithdrawWithZeroAmount() {
        HttpRequest request = HttpRequest.GET("/accounts/1");
        Account account = client.toBlocking().retrieve(request, Account.class);

        HttpRequest putRequest = HttpRequest.PUT("/accounts/1/withdraw/0", account);
        performRequestAndAssertBadStatus(putRequest, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testWithdrawNotExistedAccount() {
        HttpRequest request = HttpRequest.GET("/accounts/1");
        Account account = client.toBlocking().retrieve(request, Account.class);

        HttpRequest putRequest = HttpRequest.PUT("/accounts/100/withdraw/10", account);
        performRequestAndAssertBadStatus(putRequest, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testTransferBetweenAccounts() {
        AccountTransfer transfer = new AccountTransfer(1, 2, new BigDecimal("50.00"));
        HttpRequest request = HttpRequest.PUT("/accounts/transfer", transfer);
        HttpResponse response = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    public void testTransferBetweenAccountsWithZeroAmount() {
        AccountTransfer transfer = new AccountTransfer(1, 2, new BigDecimal("0.00"));
        HttpRequest request = HttpRequest.PUT("/accounts/transfer", transfer);
        performRequestAndAssertBadStatus(request, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testTransferOnSameAccount() {
        AccountTransfer transfer = new AccountTransfer(1, 1, new BigDecimal("50.00"));
        HttpRequest request = HttpRequest.PUT("/accounts/transfer", transfer);
        performRequestAndAssertBadStatus(request, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testTransferBetweenAccountsWithDifferentCurrencies() {
        AccountTransfer transfer = new AccountTransfer(1, 3, new BigDecimal("50.00"));
        HttpRequest request = HttpRequest.PUT("/accounts/transfer", transfer);
        performRequestAndAssertBadStatus(request, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testTransferBetweenAccountsFromNotExisted() {
        AccountTransfer transfer = new AccountTransfer(100, 2, new BigDecimal("50.00"));
        HttpRequest request = HttpRequest.PUT("/accounts/transfer", transfer);
        performRequestAndAssertBadStatus(request, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testTransferBetweenAccountsToNotExisted() {
        AccountTransfer transfer = new AccountTransfer(1, 100, new BigDecimal("50.00"));
        HttpRequest request = HttpRequest.PUT("/accounts/transfer", transfer);
        performRequestAndAssertBadStatus(request, HttpStatus.BAD_REQUEST);
    }

    @Test
    public void testTransferBetweenAccountsWithInsufficientAmount() {
        AccountTransfer transfer = new AccountTransfer(1, 2, new BigDecimal("500.00"));
        HttpRequest request = HttpRequest.PUT("/accounts/transfer", transfer);
        performRequestAndAssertBadStatus(request, HttpStatus.BAD_REQUEST);
    }
}
