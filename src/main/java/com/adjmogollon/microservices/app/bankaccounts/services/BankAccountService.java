package com.adjmogollon.microservices.app.bankaccounts.services;

import com.adjmogollon.microservices.app.bankaccounts.entity.BankAccount;
import java.util.List;

public interface BankAccountService {
    List<BankAccount> findAll();
    BankAccount findById(Long id);
    BankAccount save(BankAccount bankAccount);
    void deleteById(Long id);
    void deactivateById(Long id);
	boolean existsByAccount(String accountNumber);
	BankAccount findByAccount(String account); 
}