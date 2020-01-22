package com.mzherdev.accounts.dao;

import com.mzherdev.accounts.exception.AccountAppException;
import com.mzherdev.accounts.exception.BadRequestAppException;
import com.mzherdev.accounts.model.Account;
import com.mzherdev.accounts.util.DBHelper;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDaoImpl implements AccountDao {

    private final Logger log = Logger.getLogger(AccountDaoImpl.class);

    private final static String GET_ALL_SQL = "SELECT * FROM Account";
    private final static String GET_BY_ID_SQL = "SELECT * FROM Account WHERE id = ?";
    private final static String GET_BY_OWNER_ID_SQL = "SELECT * FROM Account WHERE ownerId = ?";
    private final static String CREATE_ACCOUNT_SQL = "INSERT INTO Account (amount, currencyCode, ownerId) VALUES (?, ?, ?)";
    private final static String DELETE_ACCOUNT_SQL = "DELETE FROM Account WHERE id = ?";
    private final static String LOCK_ACC_BY_ID_SQL = GET_BY_ID_SQL + " FOR UPDATE";
    private final static String UPDATE_ACCOUNT_SQL = "UPDATE Account SET amount = ? WHERE id = ?";

    @Override
    public List<Account> getAll() {
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_SQL)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getInt("id"), rs.getBigDecimal("amount"),
                        rs.getString("currencyCode"), rs.getInt("ownerId"));
                accounts.add(account);
            }
        } catch (SQLException e) {
            log.error("SQLException ", e);
            throw new AccountAppException(e.getMessage());
        }
        return accounts;
    }

    @Override
    public Account getById(int accountId) {
        Account account = null;
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_ID_SQL)) {
            statement.setLong(1, accountId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                account = new Account(rs.getInt("id"), rs.getBigDecimal("amount"),
                        rs.getString("currencyCode"), rs.getInt("ownerId"));
            }
        } catch (SQLException e) {
            log.error("SQLException ", e);
            throw new AccountAppException(e.getMessage());
        }
        return account;
    }

    @Override
    public List<Account> getByOwnerId(int ownerId) {
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_BY_OWNER_ID_SQL)) {
            statement.setLong(1, ownerId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getInt("id"), rs.getBigDecimal("amount"),
                        rs.getString("currencyCode"), rs.getInt("ownerId"));
                accounts.add(account);
            }
        } catch (SQLException e) {
            log.error("SQLException ", e);
            throw new AccountAppException(e.getMessage());
        }
        return accounts;
    }

    @Override
    public Account create(Account account) {
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_ACCOUNT_SQL)) {
            statement.setBigDecimal(1, account.getAmount());
            statement.setString(2, account.getCurrencyCode());
            statement.setInt(3, account.getOwnerId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new AccountAppException("Account Cannot be created");
            }
            final ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                account.setId(generatedKeys.getInt(1));
            } else {
                throw new AccountAppException("Account Cannot be created");
            }
        } catch (SQLException e) {
            log.error("SQLException ", e);
            throw new AccountAppException(e.getMessage());
        }
        return account;
    }

    @Override
    public boolean delete(int accountId) {
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ACCOUNT_SQL)) {
            statement.setLong(1, accountId);
            return statement.executeUpdate() != 0;
        } catch (SQLException e) {
            log.error("SQLException ", e);
            throw new AccountAppException(e.getMessage());
        }
    }

    @Override
    public Account updateBalance(int accountId, BigDecimal amount) {
        Account account = null;
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement lockStatement = connection.prepareStatement(LOCK_ACC_BY_ID_SQL);
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_ACCOUNT_SQL)) {
            connection.setAutoCommit(false);

            lockStatement.setLong(1, accountId);
            ResultSet rs = lockStatement.executeQuery();
            if (rs.next()) {
                account = new Account(rs.getInt("id"), rs.getBigDecimal("amount"),
                        rs.getString("currencyCode"), rs.getInt("ownerId"));
            }

            if (account == null) {
                throw new BadRequestAppException("Account not exist: " + accountId);
            }

            BigDecimal balance = account.getAmount().add(amount);
            if (BigDecimal.ZERO.compareTo(balance) >= 0) {
                throw new BadRequestAppException("Not enough money on From account: " + accountId);
            }

            updateStatement.setBigDecimal(1, balance);
            updateStatement.setLong(2, accountId);
            int updatedCount = updateStatement.executeUpdate();
            connection.commit();

            if (updatedCount > 0) {
                account = getById(accountId);
            }
        } catch (SQLException e) {
            log.error("SQLException ", e);
            throw new AccountAppException(e.getMessage());
        }
        return account;
    }

    @Override
    public boolean transfer(int accountFromId, int accountToId, BigDecimal amount) {
        int result;
        try (Connection connection = DBHelper.getConnection();
             PreparedStatement lockStatement = connection.prepareStatement(LOCK_ACC_BY_ID_SQL);
             PreparedStatement updateStatement = connection.prepareStatement(UPDATE_ACCOUNT_SQL)) {
            connection.setAutoCommit(false);

            lockStatement.setLong(1, accountFromId);
            ResultSet rs = lockStatement.executeQuery();
            Account from;
            if (rs.next()) {
                from = new Account(rs.getInt("id"), rs.getBigDecimal("amount"),
                        rs.getString("currencyCode"), rs.getInt("ownerId"));
            } else {
                throw new BadRequestAppException("From account not exists : " + accountFromId);
            }

            lockStatement.setLong(1, accountToId);
            rs = lockStatement.executeQuery();
            Account to;
            if (rs.next()) {
                to = new Account(rs.getInt("id"), rs.getBigDecimal("amount"),
                        rs.getString("currencyCode"), rs.getInt("ownerId"));
            } else {
                throw new BadRequestAppException("To account not exists : " + accountToId);
            }

            if (!from.getCurrencyCode().equals(to.getCurrencyCode())) {
                throw new BadRequestAppException("Transaction currency are different between accounts");
            }

            BigDecimal balance = from.getAmount().subtract(amount);
            if (BigDecimal.ZERO.compareTo(balance) > 0) {
                throw new BadRequestAppException("Not enough money on From account: " + accountFromId);
            }

            updateStatement.setBigDecimal(1, balance);
            updateStatement.setLong(2, accountFromId);
            updateStatement.addBatch();
            updateStatement.setBigDecimal(1, to.getAmount().add(amount));
            updateStatement.setLong(2, accountToId);
            updateStatement.addBatch();
            int[] rowsUpdated = updateStatement.executeBatch();
            result = rowsUpdated[0] + rowsUpdated[1];
            connection.commit();
        } catch (SQLException e) {
            log.error("SQLException ", e);
            throw new AccountAppException(e.getMessage());
        }
        return result == 2;
    }
}
