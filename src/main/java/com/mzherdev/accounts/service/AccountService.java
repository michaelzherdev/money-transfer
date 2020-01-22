package com.mzherdev.accounts.service;

import com.mzherdev.accounts.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    Account create(Account account);

    Account doDeposit(int accountId, BigDecimal amount);

    Account doWithdraw(int accountId, BigDecimal amount);

    boolean transfer(int accountFromId, int accountToId, BigDecimal amount);

    List<Account> getAll();

    Account getById(int accountId);

    List<Account> getByOwnerId(int ownerId);

    boolean delete(int accountId);
}
