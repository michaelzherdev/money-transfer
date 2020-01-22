package com.mzherdev.accounts.dao;

import com.mzherdev.accounts.AccountApplication;
import com.mzherdev.accounts.model.AccountOwner;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(application = AccountApplication.class)
public class AccountOwnerDaoTest {
    @Inject
    private AccountOwnerDao accountOwnerDao;

    @Test
    public void testGetAll() {
        List<AccountOwner> all = accountOwnerDao.getAll();
        assertEquals(4, all.size());
    }

    @Test
    public void testGetById() {
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
        accountOwnerDao.delete(2);
        assertNull(accountOwnerDao.getById(2));
    }

    @Test
    public void testDeleteNonExistingAccount() {
        assertNull(accountOwnerDao.getById(200));
        accountOwnerDao.delete(200);
        assertNull(accountOwnerDao.getById(200));
    }
}
