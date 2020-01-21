package com.mzherdev.accounts.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountOwner {

    private int id;
    @JsonProperty(required = true)
    private String name;
    @JsonProperty(required = true)
    private String lastName;

    public AccountOwner() {
    }

    public AccountOwner(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    public AccountOwner(int id, String name, String lastName) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
