package com.mzherdev.accounts.controller;

import com.mzherdev.accounts.model.Account;
import com.mzherdev.accounts.model.AccountOwner;
import com.mzherdev.accounts.service.AccountOwnerService;
import com.mzherdev.accounts.service.AccountService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import javax.inject.Inject;
import java.util.List;

@Controller("/owners")
@Produces(MediaType.APPLICATION_JSON)
public class AccountOwnerController {

    private AccountOwnerService ownerService;
    private AccountService accountService;

    @Inject
    public AccountOwnerController(AccountOwnerService ownerService, AccountService accountService) {
        this.ownerService = ownerService;
        this.accountService = accountService;
    }

    @Get
    public List<AccountOwner> getAll() {
        return ownerService.getAll();
    }

    @Get("/{id}")
    public AccountOwner get(int id) {
        return ownerService.getById(id);
    }

    @Get("/{id}/accounts")
    public List<Account> getOwnerAccounts(int id) {
        return accountService.getByOwnerId(id);
    }

    @Post
    public AccountOwner create(AccountOwner account) {
        return ownerService.create(account);
    }

    @Delete("/{id}")
    public HttpResponse delete(int id) {
        boolean deleted = ownerService.delete(id);
        return deleted ? HttpResponse.ok() : HttpResponse.notFound();
    }
}
