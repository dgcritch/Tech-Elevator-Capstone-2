package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.Account;


import java.math.BigDecimal;

public interface AccountDao {

    Account getAccountById(int accountId ,int userId);

    BigDecimal getAccountBalance(int accountId, int userId);

    BigDecimal getAmountToSend(int accountId, BigDecimal sendAmount, int userId);

    BigDecimal getAmountToReceive(int accountId, BigDecimal receiveAmount, int userId);
}



