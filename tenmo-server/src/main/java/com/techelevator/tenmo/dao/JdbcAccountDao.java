package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Account getAccountById(int accountId, int userId) {
        Account account = null;
        String sql = "SELECT account_id, balance, user_id FROM accounts WHERE account_id = ? AND user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, userId);
            if (results.next()) {
                account = mapRowToAccount(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return account;
    }

    @Override
    public BigDecimal getAccountBalance(int accountId, int userId) {
        BigDecimal newBalance = null;
        String sql ="SELECT balance FROM accounts WHERE account_id = ? AND user_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId, userId);
            if (results.next()) {
                newBalance = new BigDecimal(results.getString("balance"));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return newBalance;
    }

    @Transactional
    @Override
    public BigDecimal getAmountToSend(int accountId, BigDecimal sendAmount, int userId) {
        Account account = getAccountById(accountId, userId);
        if (account.getBalance().compareTo(sendAmount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        BigDecimal newBalance = account.getBalance().subtract(sendAmount);
        account.setBalance(newBalance);
        String updateSql = "UPDATE accounts SET balance = ? WHERE account_id = ? AND user_id = ?";
        jdbcTemplate.update(updateSql, newBalance, accountId, userId);
        return newBalance;
    }

    @Transactional
    @Override
    public BigDecimal getAmountToReceive(int accountId, BigDecimal receiveAmount, int userId) {
        Account account = getAccountById(accountId, userId);
        BigDecimal newBalance = account.getBalance().add(receiveAmount);
        account.setBalance(newBalance);
        String updateSql = "UPDATE accounts SET balance = ? WHERE account_id = ? AND user_id = ?";
        jdbcTemplate.update(updateSql, newBalance, accountId, userId);
        return newBalance;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setUserId(rs.getInt("user_id"));
        return account;
    }
}

