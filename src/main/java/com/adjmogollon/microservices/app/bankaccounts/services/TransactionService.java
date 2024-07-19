package com.adjmogollon.microservices.app.bankaccounts.services;

import com.adjmogollon.microservices.app.bankaccounts.entity.Transaction;
import java.util.List;

public interface TransactionService {
    List<Transaction> findAll();
    Transaction findById(Long id);
    Transaction save(Transaction transaction);
    void deleteById(Long id);
}