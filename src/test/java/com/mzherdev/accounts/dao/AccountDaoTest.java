package com.mzherdev.accounts.dao;

import com.mzherdev.accounts.AccountApplication;
import com.mzherdev.accounts.model.Account;
import com.mzherdev.accounts.model.Currency;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(application = AccountApplication.class)
public class AccountDaoTest {
    @Inject
    private AccountDao accountDao;

    @BeforeAll
    public static void setup() {
    }


    @BeforeEach
    public void beforeEach() {
    }

    @Test
    public void testGetAll() {
        List<Account> allAccounts = accountDao.getAll();
        assertEquals(4, allAccounts.size());
    }

    @Test
    public void testGetAccountById() {
        Account account = accountDao.getById(1);
        assertEquals(account.getOwnerId(), 1);
    }

    @Test
    public void testGetNonExistedAccountById() {
        assertNull(accountDao.getById(100));
    }

    @Test
    public void testGetAccountsByOwnerId() {
        List<Account> accounts = accountDao.getByOwnerId(3);
        assertEquals(2, accounts.size());
    }

    @Test
    public void testCreateAccount() {
        Account a = new Account(BigDecimal.TEN, "USD", 5);
        Account account = accountDao.create(a);
        assertEquals(5, account.getOwnerId());
        assertEquals(Currency.USD.name(), account.getCurrencyCode());
        assertEquals(BigDecimal.TEN, account.getAmount());
    }

    @Test
    public void testDeleteAccount() {
        assertNotNull(accountDao.getById(2));
        accountDao.delete(2);
        assertNull(accountDao.getById(2));
    }

    @Test
    public void testDeleteNonExistingAccount() {
        assertNull(accountDao.getById(200));
        accountDao.delete(200);
        assertNull(accountDao.getById(200));
    }

    @Test
    public void testUpdateAccountBalanceDepositSuccess() {
        Account account = accountDao.getById(1);
        account.setAmount(BigDecimal.valueOf(50L));
        Account updatedAccount = accountDao.update(account);
        assertEquals(account.getAmount(), updatedAccount.getAmount());
    }

    @Test
    public void testUpdateAccountBalanceWithdrawSuccess() {
        Account account = accountDao.getById(1);
        account.setAmount(BigDecimal.valueOf(50L).negate());
        Account updatedAccount = accountDao.update(account);
        assertEquals(account.getAmount(), updatedAccount.getAmount());
        ;
    }
}
