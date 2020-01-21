package com.mzherdev.accounts.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Account {
    private int id;
    @JsonProperty(required = true)
    private BigDecimal amount;
    @JsonProperty(required = true)
    private String currencyCode;
    @JsonProperty(required = true)
    private int ownerId;

    public Account() {
    }

    public Account(BigDecimal amount, String currencyCode, int ownerId) {
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.ownerId = ownerId;
    }

    public Account(int id, BigDecimal amount, String currencyCode, int ownerId) {
        this.id = id;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.ownerId = ownerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}
