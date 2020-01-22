package com.mzherdev.accounts.controller;

import com.mzherdev.accounts.model.Account;
import com.mzherdev.accounts.service.AccountService;
import com.mzherdev.accounts.util.ApplicationContext;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
public class AccountController {

    private AccountService accountService;

    public AccountController() {
        ApplicationContext context = ApplicationContext.getInstance();
        accountService = context.getAccountService();
    }

    @GET
    public List<Account> getAll() {
        return accountService.getAll();
    }

    @GET
    @Path("/{id}")
    public Account get(@PathParam("id") int accountId) {
        return accountService.getById(accountId);
    }

    @POST
    public Account create(Account account) {
        return accountService.create(account);
    }

    @PUT
    @Path("/{id}/deposit/{amount}")
    public Account deposit(@PathParam("id") int accountId, @PathParam("amount") BigDecimal amount) {
        return accountService.doDeposit(accountId, amount);
    }

    @PUT
    @Path("/{id}/withdraw/{amount}")
    public Account withdraw(@PathParam("id") int accountId, @PathParam("amount") BigDecimal amount) {
        return accountService.doWithdraw(accountId, amount);
    }

    @PUT
    @Path("/transfer")
    public boolean transfer(@QueryParam("accountFromId") int accountFromId,
                            @QueryParam("accountToId") int accountToId,
                            @QueryParam("amount") BigDecimal amount) {
        return accountService.transfer(accountFromId, accountToId, amount);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAccount(@PathParam("id") int accountId) {
        if (accountService.delete(accountId)) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

}
