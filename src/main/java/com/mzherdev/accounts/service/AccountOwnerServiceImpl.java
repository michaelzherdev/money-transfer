package com.mzherdev.accounts.service;

import com.mzherdev.accounts.dao.AccountOwnerDao;
import com.mzherdev.accounts.exception.BadRequestAppException;
import com.mzherdev.accounts.model.AccountOwner;

import java.util.List;

public class AccountOwnerServiceImpl implements AccountOwnerService {

    private AccountOwnerDao ownerDao;

    public AccountOwnerServiceImpl(AccountOwnerDao ownerDao) {
        this.ownerDao = ownerDao;
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
        return ownerDao.delete(id);
    }
}
