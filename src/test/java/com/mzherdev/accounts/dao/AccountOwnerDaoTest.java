package com.mzherdev.accounts.dao;

import com.mzherdev.accounts.model.AccountOwner;
import com.mzherdev.accounts.util.DBHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AccountOwnerDaoTest {
    private static AccountOwnerDao accountOwnerDao;

    @BeforeAll
    public static void setup() {
        accountOwnerDao = new AccountOwnerDaoImpl();
    }

    @BeforeEach
    public void beforeEach() {
        DBHelper.populateTestData();
    }

    @Test
    public void testGetAll() {
        List<AccountOwner> all = accountOwnerDao.getAll();
        assertEquals(4, all.size());
    }

    @Test
    public void testGetAccountById() {
        AccountOwner account = accountOwnerDao.getById(1);
        assertEquals(account.getName(), "test1");
        assertEquals(account.getLastName(), "test111");
    }

    @Test
    public void testGetNonExistedAccountById() {
        assertNull(accountOwnerDao.getById(100));
    }

    @Test
    public void testCreateAccount() {
        AccountOwner a = new AccountOwner("new1", "new111");
        AccountOwner account = accountOwnerDao.create(a);
        assertEquals(5, account.getId());
        assertEquals("new1", account.getName());
        assertEquals("new111", account.getLastName());
    }

    @Test
    public void testDeleteAccount() {
        assertNotNull(accountOwnerDao.getById(2));
        assertTrue(accountOwnerDao.delete(2));
        assertNull(accountOwnerDao.getById(2));
    }

    @Test
    public void testDeleteNonExistingAccount() {
        assertFalse(accountOwnerDao.delete(200));
    }
}
