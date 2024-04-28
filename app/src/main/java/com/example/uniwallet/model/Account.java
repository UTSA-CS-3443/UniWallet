package com.example.uniwallet.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Account implements Serializable {


    private int userID = 0;
    private String username;
    private String password;
    private String category; //expense
    private String rate; //expense
    private double cost; //expense
    private double pay; //income
    private String timeRate; //income
    private double budget; //info
    private double balance; //info
    private int transactionNumber; //quickAdd
    private double amount; //quickAdd
    private int transactionNumber2; //quickRemove
    private String category2; //quickRemove
    private String item; //quickRemove
    private double amount2; //quickRemove
    private double savings;
    public Account(int userID, String username, String password) {


        this.username = username;
        this.password = password;
        this.userID = userID;

        this.setCategory(null);
        this.setRate(null);
        this.setCost(0.0);
        this.pay = 0.0;
        this.setTimeRate(null);
        this.budget = 0.0;
        this.balance = 0.0;
        this.savings = 0.0;
        this.setTransactionNumber(0);
        this.setAmount(0.0);
        this.setTransactionNumber2(0);
        this.setCategory2(null);
        this.setItem(null);
        this.setAmount2(0.0);
    }

    public void setDefaultValues() {

    }

    public double getBudget() {
        return budget;
    }
    public void setBudget(double budget){ this.budget = budget; }
    public double getBalance() {
        return balance;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public int getUserID() {
        return userID;
    }
    public double getSavings() {return savings;}
    public void setSavings(double savings){this.savings = savings;}
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }

    public String getTimeRate() {
        return timeRate;
    }

    public void setTimeRate(String timeRate) {
        this.timeRate = timeRate;
    }

    public int getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(int transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getTransactionNumber2() {
        return transactionNumber2;
    }

    public void setTransactionNumber2(int transactionNumber2) {
        this.transactionNumber2 = transactionNumber2;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public double getAmount2() {
        return amount2;
    }

    public void setAmount2(double amount2) {
        this.amount2 = amount2;
    }

}


