package com.mzherdev.accounts.service;

import com.mzherdev.accounts.dao.AccountDao;
import com.mzherdev.accounts.dao.AccountOwnerDao;
import com.mzherdev.accounts.exception.BadRequestAppException;
import com.mzherdev.accounts.model.Account;
import com.mzherdev.accounts.model.Currency;

import java.math.BigDecimal;
import java.util.List;

public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;
    private final AccountOwnerDao accountOwnerDao;

    public AccountServiceImpl(AccountDao accountDao, AccountOwnerDao accountOwnerDao) {
        this.accountDao = accountDao;
        this.accountOwnerDao = accountOwnerDao;
    }

    public Account create(Account account) {
        if(accountOwnerDao.getById(account.getOwnerId()) == null) {
            throw new BadRequestAppException("Account Owner not exists");
        }
        if (BigDecimal.ZERO.compareTo(account.getAmount()) > 0) {
            throw new BadRequestAppException("Balance is negative");
        }
        if (!Currency.isValidCode(account.getCurrencyCode())) {
            throw new BadRequestAppException("Currency not valid");
        }
        return accountDao.create(account);
    }

    public Account doDeposit(int accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) >= 0) {
            throw new BadRequestAppException("Invalid Deposit amount");
        }
        return accountDao.updateBalance(accountId, amount);
    }

    public Account doWithdraw(int accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) >= 0) {
            throw new BadRequestAppException("Invalid Withdraw amount");
        }
        return accountDao.updateBalance(accountId, amount.negate());
    }

    public boolean transfer(int accountFromId, int accountToId, BigDecimal amount) {
        if (accountFromId == accountToId) {
            throw new BadRequestAppException("Impossible to transfer money on the same account");
        }
        if (BigDecimal.ZERO.compareTo(amount) >= 0) {
            throw new BadRequestAppException("Invalid Transfer amount");
        }
        return accountDao.transfer(accountFromId, accountToId, amount);
    }

    public List<Account> getAll() {
        return accountDao.getAll();
    }

    public Account getById(int accountId) {
        return accountDao.getById(accountId);
    }

    @Override
    public List<Account> getByOwnerId(int ownerId) {
        return accountDao.getByOwnerId(ownerId);
    }

    public boolean delete(int accountId) {
        return accountDao.delete(accountId);
    }
}
