package com.mzherdev.accounts.dao;

import com.mzherdev.accounts.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    List<Account> getAll();

    Account getById(int accountId);

    Account create(Account account);

    boolean delete(int accountId);

    Account updateBalance(int accountId, BigDecimal amount);

    boolean transfer(int accountFromId, int accountToId, BigDecimal amount);
}
