package com.mzherdev.accounts.util;

import com.mzherdev.accounts.dao.AccountDao;
import com.mzherdev.accounts.dao.AccountDaoImpl;
import com.mzherdev.accounts.dao.AccountOwnerDao;
import com.mzherdev.accounts.dao.AccountOwnerDaoImpl;
import com.mzherdev.accounts.service.AccountOwnerService;
import com.mzherdev.accounts.service.AccountOwnerServiceImpl;
import com.mzherdev.accounts.service.AccountService;
import com.mzherdev.accounts.service.AccountServiceImpl;

public final class ApplicationContext {

    private AccountDao accountDao;
    private AccountOwnerDao accountOwnerDao;
    private AccountService accountService;
    private AccountOwnerService accountOwnerService;

    private ApplicationContext() {
    }

    public static ApplicationContext getInstance() {
        return ContextInstanceHolder.INSTANCE;
    }

    private static class ContextInstanceHolder {
        private static final ApplicationContext INSTANCE = new ApplicationContext();
    }

    public AccountDao getAccountDao() {
        if (accountDao == null) {
            accountDao = new AccountDaoImpl();
        }
        return accountDao;
    }

    public AccountOwnerDao getAccountOwnerDao() {
        if (accountOwnerDao == null) {
            accountOwnerDao = new AccountOwnerDaoImpl();
        }
        return accountOwnerDao;
    }

    public AccountService getAccountService() {
        if (accountService == null) {
            accountService = new AccountServiceImpl(getAccountDao(), getAccountOwnerDao());
        }
        return accountService;
    }

    public AccountOwnerService getAccountOwnerService() {
        if (accountOwnerService == null) {
            accountOwnerService = new AccountOwnerServiceImpl(getAccountOwnerDao(), getAccountDao());
        }
        return accountOwnerService;
    }
}
