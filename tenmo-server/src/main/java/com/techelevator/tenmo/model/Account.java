package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private int accountId;
    private BigDecimal balance;
    private int userId;

    public Account() {}

    public Account(int accountId, BigDecimal balance, int userId) {
        this.accountId = accountId;
        this.balance = balance;
        this.userId = userId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Account{" +
                "account_id=" + accountId +
                ", balance=" + balance +
                ", user_id=" + userId +
                '}';
    }

}
