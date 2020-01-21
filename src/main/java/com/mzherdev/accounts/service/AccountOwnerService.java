package com.mzherdev.accounts.service;

import com.mzherdev.accounts.model.AccountOwner;

import java.util.List;

public interface AccountOwnerService {

    List<AccountOwner> getAll();

    AccountOwner getById(int id);

    AccountOwner create(AccountOwner owner);

    boolean delete(int id);
}
