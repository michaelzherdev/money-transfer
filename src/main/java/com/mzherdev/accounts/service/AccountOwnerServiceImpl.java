package com.mzherdev.accounts.service;

import com.mzherdev.accounts.dao.AccountDao;
import com.mzherdev.accounts.dao.AccountOwnerDao;
import com.mzherdev.accounts.exception.BadRequestAppException;
import com.mzherdev.accounts.model.Account;
import com.mzherdev.accounts.model.AccountOwner;

import java.util.List;

public class AccountOwnerServiceImpl implements AccountOwnerService {

    private AccountOwnerDao ownerDao;
    private AccountDao accountDao;

    public AccountOwnerServiceImpl(AccountOwnerDao ownerDao, AccountDao accountDao) {
        this.ownerDao = ownerDao;
        this.accountDao = accountDao;
    }

    @Override
    public List<AccountOwner> getAll() {
        return ownerDao.getAll();
    }

    @Override
    public AccountOwner getById(int id) {
        return ownerDao.getById(id);
    }

    @Override
    public AccountOwner create(AccountOwner owner) {
        if (owner.getName() == null || owner.getName().isEmpty()) {
            throw new BadRequestAppException("Account Owner name is required");
        }
        if (owner.getLastName() == null || owner.getLastName().isEmpty()) {
            throw new BadRequestAppException("Account Owner lastName is required");
        }
        return ownerDao.create(owner);
    }

    @Override
    public boolean delete(int id) {
        List<Account> accounts = accountDao.getByOwnerId(id);
        if (accounts != null && !accounts.isEmpty()) {
            throw new BadRequestAppException("Owner has opened accounts, cannot be removed");
        }
        return ownerDao.delete(id);
    }
}
