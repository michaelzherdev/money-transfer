package com.mzherdev.accounts.service;

import com.mzherdev.accounts.dao.AccountDao;
import com.mzherdev.accounts.dao.AccountOwnerDao;
import com.mzherdev.accounts.exception.BadRequestAppException;
import com.mzherdev.accounts.model.Account;
import com.mzherdev.accounts.model.Currency;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.util.List;

@Singleton
public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;
    private final AccountOwnerDao accountOwnerDao;

    @Inject
    public AccountServiceImpl(AccountDao accountDao, AccountOwnerDao accountOwnerDao) {
        this.accountDao = accountDao;
        this.accountOwnerDao = accountOwnerDao;
    }

    @Override
    @Transactional
    public Account create(Account account) {
        if (accountOwnerDao.getById(account.getOwnerId()) == null) {
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

    @Override
    @Transactional
    public Account doDeposit(int accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) >= 0) {
            throw new BadRequestAppException("Invalid Deposit amount");
        }
        Account account = accountDao.getById(accountId);
        if (account == null) {
            throw new BadRequestAppException("Account does not exist");
        }
        account.setAmount(account.getAmount().add(amount));
        return accountDao.update(account);
    }

    @Override
    @Transactional
    public Account doWithdraw(int accountId, BigDecimal amount) {
        if (BigDecimal.ZERO.compareTo(amount) >= 0) {
            throw new BadRequestAppException("Invalid Deposit amount");
        }
        Account account = accountDao.getById(accountId);
        if (account == null) {
            throw new BadRequestAppException("Account does not exist");
        }
        final BigDecimal amountAfterWithdraw = account.getAmount().subtract(amount);
        if (amountAfterWithdraw.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestAppException("Not enough money on From account: " + accountId);
        }
        account.setAmount(amountAfterWithdraw);
        return accountDao.update(account);
    }

    @Override
    @Transactional
    public boolean transfer(int accountFromId, int accountToId, BigDecimal amount) {
        if (accountFromId == accountToId) {
            throw new BadRequestAppException("Impossible to transfer money on the same account");
        }
        if (BigDecimal.ZERO.compareTo(amount) >= 0) {
            throw new BadRequestAppException("Invalid Transfer amount");
        }
        Account from = accountDao.getById(accountFromId);
        if (from == null) {
            throw new BadRequestAppException("From account not exists : " + accountFromId);
        }
        Account to = accountDao.getById(accountToId);
        if (to == null) {
            throw new BadRequestAppException("To account not exists : " + accountToId);
        }
        if (!from.getCurrencyCode().equals(to.getCurrencyCode())) {
            throw new BadRequestAppException("Transaction currency are different between accounts");
        }

        BigDecimal balance = from.getAmount().subtract(amount);
        if (BigDecimal.ZERO.compareTo(balance) > 0) {
            throw new BadRequestAppException("Not enough money on From account: " + accountFromId);
        }

        from.setAmount(balance);
        accountDao.update(from);
        from.setAmount(to.getAmount().add(amount));
        accountDao.update(from);

        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> getAll() {
        return accountDao.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Account getById(int accountId) {
        return accountDao.getById(accountId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> getByOwnerId(int ownerId) {
        return accountDao.getByOwnerId(ownerId);
    }

    @Override
    @Transactional
    public boolean delete(int accountId) {
        return accountDao.delete(accountId);
    }
}
