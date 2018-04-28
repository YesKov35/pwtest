package com.example.lnik3.pwtest;

/**
 * Created by lnik3 on 18.04.2018.
 */

public class User {
    private String id;
    private String balance;
    private String email;
    private String join_date;
    private String label;
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJoin_date() {
        return join_date;
    }

    public void setJoin_date(String join_date) {
        this.join_date = join_date;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", balance = " + balance + ", email = " + email + ", join_date = " + join_date + ", label = " + label + ", password = " + password + "]";
    }
}
