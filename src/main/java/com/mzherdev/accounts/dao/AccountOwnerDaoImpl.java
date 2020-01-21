package com.mzherdev.accounts.dao;

import com.mzherdev.accounts.exception.AccountAppException;
import com.mzherdev.accounts.model.AccountOwner;
import com.mzherdev.accounts.util.DBHelper;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountOwnerDaoImpl implements AccountOwnerDao {

    private final Logger log = Logger.getLogger(AccountOwnerDaoImpl.class);

    private final static String GET_ALL_SQL = "SELECT * FROM AccountOwner";
    private final static String GET_BY_ID_SQL = "SELECT * FROM AccountOwner WHERE id = ?";
    private final static String CREATE_ACCOUNT_OWNER_SQL = "INSERT INTO AccountOwner (name, lastName) VALUES (?, ?)";
    private final static String DELETE_ACCOUNT_OWNER_SQL = "DELETE FROM AccountOwner WHERE id = ?";

    @Override
    public List<AccountOwner> getAll() {
        List<AccountOwner> owners = new ArrayList<>();
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_SQL)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                AccountOwner accountOwner = new AccountOwner(rs.getInt("id"),
                        rs.getString("name"), rs.getString("lastName"));
                owners.add(accountOwner);
            }
        } catch (SQLException e) {
            log.error("SQLException ", e);
            throw new AccountAppException(e.getMessage());
        }
        return owners;
    }

    @Override
    public AccountOwner getById(int id) {
        AccountOwner owner = null;
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_ID_SQL)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                owner = new AccountOwner(rs.getInt("id"),
                        rs.getString("name"), rs.getString("lastName"));
            }
        } catch (SQLException e) {
            log.error("SQLException ", e);
            throw new AccountAppException(e.getMessage());
        }
        return owner;
    }

    @Override
    public AccountOwner create(AccountOwner accountOwner) {
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_ACCOUNT_OWNER_SQL)) {
            statement.setString(1, accountOwner.getName());
            statement.setString(2, accountOwner.getLastName());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new AccountAppException("Account Owner Cannot be created");
            }
            final ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                accountOwner.setId(generatedKeys.getInt(1));
            } else {
                throw new AccountAppException("Account Owner Cannot be created");
            }
        } catch (SQLException e) {
            log.error("SQLException ", e);
            throw new AccountAppException(e.getMessage());
        }
        return accountOwner;
    }

    @Override
    public boolean delete(int ownerId) {
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ACCOUNT_OWNER_SQL)) {
            statement.setLong(1, ownerId);
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            log.error("SQLException ", e);
            throw new AccountAppException(e.getMessage());
        }
    }
}
