package com.mzherdev.accounts.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "Account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    @NotNull
    @Column(name = "currencyCode", nullable = false)
    private String currencyCode;
    @NotNull
    @Column(name = "ownerId", nullable = false)
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
