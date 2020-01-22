package com.mzherdev.accounts.dao;

import com.mzherdev.accounts.model.Account;

import java.util.List;

public interface AccountDao {

    List<Account> getAll();

    Account getById(int accountId);

    List<Account> getByOwnerId(int ownerId);

    Account create(Account account);

    boolean delete(int accountId);

    Account update(Account account);
}
