package com.mzherdev.accounts.controller;

import com.mzherdev.accounts.model.Account;
import com.mzherdev.accounts.model.dto.AccountTransfer;
import com.mzherdev.accounts.service.AccountService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@Controller("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountController {

    @Inject
    private AccountService accountService;

    @Get("/")
    public List<Account> getAll() {
        return accountService.getAll();
    }

    @Get("/{id}")
    public Account getAccount(int id) {
        return accountService.getById(id);
    }

    @Post("/")
    public Account createAccount(@Body @Valid Account account) {
        return accountService.create(account);
    }

    @Put("/{id}/deposit/{amount}")
    public Account deposit(int id, BigDecimal amount) {
        return accountService.doDeposit(id, amount);
    }

    @Put("/{id}/withdraw/{amount}")
    public Account withdraw(int id, BigDecimal amount) {
        return accountService.doWithdraw(id, amount);
    }

    @Put("/transfer")
    public boolean transfer(@Body @Valid AccountTransfer transfer) {
        return accountService.transfer(transfer.getAccountFromId(), transfer.getAccountToId(), transfer.getAmount());
    }

    @Delete("/{id}")
    public HttpResponse deleteAccount(int id) {
        final boolean deleted = accountService.delete(id);
        return deleted ? HttpResponse.ok() : HttpResponse.notFound();
    }
}
