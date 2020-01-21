package com.mzherdev.accounts.controller;

import com.mzherdev.accounts.model.AccountOwner;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountOwnerControllerTest extends AbstractControllerTest {

    @Test
    public void testGetById() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/owners/1").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(200, statusCode);
        AccountOwner account = mapper.readValue(EntityUtils.toString(response.getEntity()), AccountOwner.class);
        assertEquals(1, account.getId());
        assertEquals("test1", account.getName());
        assertEquals("test111", account.getLastName());
    }

    @Test
    public void testGetAll() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/owners").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(200, statusCode);
        String jsonString = EntityUtils.toString(response.getEntity());
        AccountOwner[] accounts = mapper.readValue(jsonString, AccountOwner[].class);
        assertEquals(4, accounts.length);
    }

    @Test
    public void testCreateAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/owners").build();
        AccountOwner account = new AccountOwner("new1", "new111");
        String jsonInString = mapper.writeValueAsString(account);
        StringEntity entity = new StringEntity(jsonInString);

        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();

        assertEquals(200, statusCode);
        String jsonString = EntityUtils.toString(response.getEntity());
        AccountOwner afterCreation = mapper.readValue(jsonString, AccountOwner.class);
        assertEquals(5, afterCreation.getId());
        assertEquals("new1", account.getName());
        assertEquals("new111", account.getLastName());
    }

    @Test
    public void testCreateAccountWithEmptyName() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/owners").build();
        AccountOwner acc = new AccountOwner(null, "lastName");
        String jsonInString = mapper.writeValueAsString(acc);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(400, statusCode);
        assertEquals("Account Owner name is required", EntityUtils.toString(response.getEntity()));
    }

    @Test
    public void testCreateAccountWithEmptyLastName() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/owners").build();
        AccountOwner acc = new AccountOwner("name", "");
        String jsonInString = mapper.writeValueAsString(acc);
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(400, statusCode);
        assertEquals("Account Owner lastName is required", EntityUtils.toString(response.getEntity()));
    }


    @Test
    public void testDeleteAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/owners/2").build();
        HttpDelete request = new HttpDelete(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);
    }

    @Test
    public void testDeleteNonExistingAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/owners/200").build();
        HttpDelete request = new HttpDelete(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(404, statusCode);
    }
}
