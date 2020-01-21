package com.mzherdev.accounts.controller;

import com.mzherdev.accounts.model.AccountOwner;
import com.mzherdev.accounts.service.AccountOwnerService;
import com.mzherdev.accounts.util.ApplicationContext;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/owners")
@Produces(MediaType.APPLICATION_JSON)
public class AccountOwnerController {

    private AccountOwnerService ownerService;

    public AccountOwnerController() {
        ApplicationContext context = ApplicationContext.getInstance();
        this.ownerService = context.getAccountOwnerService();
    }

    @GET
    public List<AccountOwner> getAll() {
        return ownerService.getAll();
    }

    @GET
    @Path("/{id}")
    public AccountOwner getAccount(@PathParam("id") int id) {
        return ownerService.getById(id);
    }

    @POST
    public AccountOwner createAccount(AccountOwner account) {
        return ownerService.create(account);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAccount(@PathParam("id") int id) {
        if (ownerService.delete(id)) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
