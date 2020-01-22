package com.mzherdev.accounts.controller;

import com.mzherdev.accounts.model.Account;
import com.mzherdev.accounts.model.Currency;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountControllerTest extends AbstractControllerTest {

    @Test
    public void testGetById() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts/1").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(200, statusCode);
        Account account = mapper.readValue(EntityUtils.toString(response.getEntity()), Account.class);
        assertEquals(1, account.getOwnerId());
        assertEquals(new BigDecimal("100.00"), account.getAmount());
    }

    @Test
    public void testGetAll() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(200, statusCode);
        String jsonString = EntityUtils.toString(response.getEntity());
        Account[] accounts = mapper.readValue(jsonString, Account[].class);
        assertEquals(4, accounts.length);
    }

    @Test
    public void testCreateAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts").build();
        Account account = new Account(BigDecimal.TEN, "USD", 1);
        String jsonInString = mapper.writeValueAsString(account);
        StringEntity entity = new StringEntity(jsonInString);

        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(200, statusCode);
        String jsonString = EntityUtils.toString(response.getEntity());
        Account afterCreation = mapper.readValue(jsonString, Account.class);
        assertEquals(5, afterCreation.getId());
        assertEquals(1, afterCreation.getOwnerId());
        assertEquals(Currency.USD.name(), afterCreation.getCurrencyCode());
    }

    @Test
    public void testCreateAccountWithUniexistedOwner() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts").build();
        Account acc = new Account(BigDecimal.ONE, "USD", 100);
        String jsonInString = mapper.writeValueAsString(acc);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(400, statusCode);
        assertEquals("Account Owner not exists", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testCreateAccountWithNegativeBalance() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts").build();
        Account acc = new Account(BigDecimal.ONE.negate(), "USD", 1);
        String jsonInString = mapper.writeValueAsString(acc);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(400, statusCode);
        assertEquals("Balance is negative", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testCreateAccountWithUnexistedCurrencyBalance() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts").build();
        Account acc = new Account(BigDecimal.ONE, "RUS", 1);
        String jsonInString = mapper.writeValueAsString(acc);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(400, statusCode);
        assertEquals("Currency not valid", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testDeleteAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts/2").build();
        HttpDelete request = new HttpDelete(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void testDeleteNonExistingAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts/200").build();
        HttpDelete request = new HttpDelete(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(404, statusCode);
    }

    @Test
    public void testDepositSuccessful() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts/1").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(200, statusCode);
        Account account = mapper.readValue(EntityUtils.toString(response.getEntity()), Account.class);
        assertEquals(1, account.getOwnerId());
        assertEquals(new BigDecimal("100.00"), account.getAmount());

        uri = builder.setPath("/accounts/1/deposit/10")
                .build();
        HttpPut depositRequest = new HttpPut(uri);
        depositRequest.setHeader("Content-type", "application/json");
        response = client.execute(depositRequest);
        statusCode = response.getStatusLine().getStatusCode();

        assertEquals(200, statusCode);
        String jsonString = EntityUtils.toString(response.getEntity());
        account = mapper.readValue(jsonString, Account.class);
        assertEquals(new BigDecimal("110.00"), account.getAmount());
    }

    @Test
    public void testDepositWithZeroAmount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts/1/deposit/0")
                .build();
        HttpPut depositRequest = new HttpPut(uri);
        depositRequest.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(depositRequest);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(400, statusCode);
        assertEquals("Invalid Deposit amount", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testDepositNotExistedAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts/100/deposit/10")
                .build();
        HttpPut depositRequest = new HttpPut(uri);
        depositRequest.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(depositRequest);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(400, statusCode);
        assertEquals("Account not exist: 100", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testWithdrawSuccessful() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts/1").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(200, statusCode);
        Account account = mapper.readValue(EntityUtils.toString(response.getEntity()), Account.class);
        assertEquals(1, account.getOwnerId());
        assertEquals(new BigDecimal("100.00"), account.getAmount());

        uri = builder.setPath("/accounts/1/withdraw/10")
                .build();
        HttpPut depositRequest = new HttpPut(uri);
        depositRequest.setHeader("Content-type", "application/json");
        response = client.execute(depositRequest);
        statusCode = response.getStatusLine().getStatusCode();

        assertEquals(200, statusCode);
        String jsonString = EntityUtils.toString(response.getEntity());
        account = mapper.readValue(jsonString, Account.class);
        assertEquals(new BigDecimal("90.00"), account.getAmount());
    }

    @Test
    public void testWithdrawWithZeroAmount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts/1/withdraw/0")
                .build();
        HttpPut depositRequest = new HttpPut(uri);
        depositRequest.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(depositRequest);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(400, statusCode);
        assertEquals("Invalid Withdraw amount", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testWithdrawNotExistedAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts/100/withdraw/10")
                .build();
        HttpPut depositRequest = new HttpPut(uri);
        depositRequest.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(depositRequest);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(400, statusCode);
        assertEquals("Account not exist: 100", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testTransferBetweenAcconts() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts/transfer")
                .addParameter("accountFromId", "1")
                .addParameter("accountToId", "2")
                .addParameter("amount", "50")
                .build();
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(200, statusCode);
        String jsonString = EntityUtils.toString(response.getEntity());
        assertTrue(mapper.readValue(jsonString, Boolean.class));
    }

    @Test
    public void testTransferBetweenAccountsWithZeroAmount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts/transfer")
                .addParameter("accountFromId", "1")
                .addParameter("accountToId", "2")
                .addParameter("amount", "0")
                .build();
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(400, statusCode);
        assertEquals("Invalid Transfer amount", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testTransferOnSameAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts/transfer")
                .addParameter("accountFromId", "1")
                .addParameter("accountToId", "1")
                .addParameter("amount", "100")
                .build();
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(400, statusCode);
        assertEquals("Impossible to transfer money on the same account", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testTransferBetweenAccountsWithDifferentCurrencies() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts/transfer")
                .addParameter("accountFromId", "1")
                .addParameter("accountToId", "3")
                .addParameter("amount", "100")
                .build();
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(400, statusCode);
        assertEquals("Transaction currency are different between accounts", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testTransferBetweenAccountsFromNotExisted() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts/transfer")
                .addParameter("accountFromId", "100")
                .addParameter("accountToId", "1")
                .addParameter("amount", "100")
                .build();
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(400, statusCode);
        assertEquals("From account not exists : 100", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testTransferBetweenAccountsToNotExisted() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts/transfer")
                .addParameter("accountFromId", "1")
                .addParameter("accountToId", "100")
                .addParameter("amount", "100")
                .build();
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(400, statusCode);
        assertEquals("To account not exists : 100", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testTransferBetweenAccountsWithInsufficientAmount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/accounts/transfer")
                .addParameter("accountFromId", "1")
                .addParameter("accountToId", "2")
                .addParameter("amount", "500")
                .build();
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(400, statusCode);
        assertEquals("Not enough money on From account: 1", EntityUtils.toString(response.getEntity()));
    }
}
