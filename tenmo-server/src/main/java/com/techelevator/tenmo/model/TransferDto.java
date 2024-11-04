package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class TransferDto {

    private int accountFrom;
    private int accountTo;
    private BigDecimal amount;

    public TransferDto() {
    }



    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
