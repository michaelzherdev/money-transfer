package com.mzherdev.accounts.dao;

import com.mzherdev.accounts.model.AccountOwner;

import java.util.List;

public interface AccountOwnerDao {

    List<AccountOwner> getAll();

    AccountOwner getById(int id);

    AccountOwner create(AccountOwner account);

    boolean delete(int id);
}
