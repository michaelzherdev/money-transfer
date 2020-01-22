package com.mzherdev.accounts.service;

import com.mzherdev.accounts.dao.AccountDao;
import com.mzherdev.accounts.dao.AccountOwnerDao;
import com.mzherdev.accounts.exception.BadRequestAppException;
import com.mzherdev.accounts.model.Account;
import com.mzherdev.accounts.model.AccountOwner;
import io.micronaut.spring.tx.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class AccountOwnerServiceImpl implements AccountOwnerService {

    private final AccountDao accountDao;
    private final AccountOwnerDao ownerDao;

    @Inject
    public AccountOwnerServiceImpl(AccountDao accountDao, AccountOwnerDao accountOwnerDao) {
        this.accountDao = accountDao;
        this.ownerDao = accountOwnerDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountOwner> getAll() {
        return ownerDao.getAll();
    }

    @Override
    @Transactional(readOnly = true)
    public AccountOwner getById(int id) {
        return ownerDao.getById(id);
    }

    @Override
    @Transactional
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
    @Transactional
    public boolean delete(int id) {
        List<Account> accounts = accountDao.getByOwnerId(id);
        if (accounts != null && !accounts.isEmpty()) {
            throw new BadRequestAppException("Owner has opened accounts, cannot be removed");
        }
        return ownerDao.delete(id);
    }
}
