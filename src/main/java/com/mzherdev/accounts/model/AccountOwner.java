package com.mzherdev.accounts.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "AccountOwner")
public class AccountOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;
    @NotNull
    @Column(name = "lastName", nullable = false)
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
