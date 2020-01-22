package com.mzherdev.accounts.dao;

import com.mzherdev.accounts.model.AccountOwner;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Singleton
public class AccountOwnerDaoImpl implements AccountOwnerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AccountOwner> getAll() {
        String sql = "SELECT ao FROM AccountOwner as ao";
        TypedQuery<AccountOwner> query = entityManager.createQuery(sql, AccountOwner.class);
        return query.getResultList();
    }

    @Override
    public AccountOwner getById(int id) {
        return entityManager.find(AccountOwner.class, id);
    }

    @Override
    public AccountOwner create(AccountOwner accountOwner) {
        entityManager.persist(accountOwner);
        return accountOwner;
    }

    @Override
    public boolean delete(int id) {
        AccountOwner owner = getById(id);
        if (owner != null) {
            entityManager.remove(owner);
            return true;
        } else {
            return false;
        }
    }
}
