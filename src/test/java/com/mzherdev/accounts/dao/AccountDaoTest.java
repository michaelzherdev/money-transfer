package com.mzherdev.accounts.dao;

import com.mzherdev.accounts.exception.AccountAppException;
import com.mzherdev.accounts.model.Account;
import com.mzherdev.accounts.model.Currency;
import com.mzherdev.accounts.util.DBHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountDaoTest {

    private static AccountDao accountDao;

    @BeforeAll
    public static void setup() {
        accountDao = new AccountDaoImpl();
    }


    @BeforeEach
    public void beforeEach() {
        DBHelper.populateTestData();
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
        assertTrue(accountDao.delete(2));
        assertNull(accountDao.getById(2));
    }

    @Test
    public void testDeleteNonExistingAccount() {
        assertFalse(accountDao.delete(200));
    }

    @Test
    public void testUpdateAccountBalanceDepositSuccess() {
        Account account = accountDao.getById(1);
        final BigDecimal fifty = BigDecimal.valueOf(50L);
        Account updatedAccount = accountDao.updateBalance(1, fifty);
        assertEquals(account.getAmount().add(fifty), updatedAccount.getAmount());
    }

    @Test
    public void testUpdateAccountBalanceWithdrawSuccess() {
        Account account = accountDao.getById(2);
        final BigDecimal minusFifty = BigDecimal.valueOf(-50L);
        Account updatedAccount = accountDao.updateBalance(2, minusFifty);
        assertEquals(account.getAmount().add(minusFifty), updatedAccount.getAmount());
    }

    @Test
    public void testUpdateAccountBalanceNotEnoughFund() {
        Assertions.assertThrows(AccountAppException.class, () -> accountDao.updateBalance(1, BigDecimal.valueOf(-100_000)));
    }
}
