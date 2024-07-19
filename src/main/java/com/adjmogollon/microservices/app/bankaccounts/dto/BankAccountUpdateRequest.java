package com.adjmogollon.microservices.app.bankaccounts.dto;

public class BankAccountUpdateRequest {



    private String accountNumber;
    private Double amount;

    // Getters and Setters
    
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}