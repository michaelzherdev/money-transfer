package com.mzherdev.accounts.dao;

import com.mzherdev.accounts.model.Account;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Singleton
public class AccountDaoImpl implements AccountDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Account> getAll() {
        String sql = "SELECT a FROM Account as a";
        TypedQuery<Account> query = entityManager.createQuery(sql, Account.class);
        return query.getResultList();
    }

    @Override
    public Account getById(int id) {
        return entityManager.find(Account.class, id);
    }

    @Override
    public List<Account> getByOwnerId(int ownerId) {
        String sql = "SELECT a FROM Account as a WHERE a.ownerId = :ownerId";
        TypedQuery<Account> query = entityManager.createQuery(sql, Account.class)
                .setParameter("ownerId", ownerId);
        return query.getResultList();
    }

    @Override
    public Account create(Account account) {
        entityManager.persist(account);
        return account;
    }

    @Override
    public boolean delete(int id) {
        Account account = getById(id);
        if (account != null) {
            entityManager.remove(account);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Account update(Account account) {
        return entityManager.merge(account);
    }
}
