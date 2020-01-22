package com.mzherdev.accounts.controller;

import com.mzherdev.accounts.model.Account;
import com.mzherdev.accounts.model.AccountOwner;
import com.mzherdev.accounts.service.AccountOwnerService;
import com.mzherdev.accounts.service.AccountService;
import com.mzherdev.accounts.util.ApplicationContext;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/owners")
@Produces(MediaType.APPLICATION_JSON)
public class AccountOwnerController {

    private AccountOwnerService ownerService;
    private AccountService accountService;

    public AccountOwnerController() {
        ApplicationContext context = ApplicationContext.getInstance();
        this.ownerService = context.getAccountOwnerService();
        this.accountService = context.getAccountService();
    }

    @GET
    public List<AccountOwner> getAll() {
        return ownerService.getAll();
    }

    @GET
    @Path("/{id}")
    public AccountOwner get(@PathParam("id") int id) {
        return ownerService.getById(id);
    }

    @GET
    @Path("/{id}/accounts")
    public List<Account> getOwnerAccounts(@PathParam("id") int id) {
        return accountService.getByOwnerId(id);
    }

    @POST
    public AccountOwner create(AccountOwner account) {
        return ownerService.create(account);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id) {
        if (ownerService.delete(id)) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
