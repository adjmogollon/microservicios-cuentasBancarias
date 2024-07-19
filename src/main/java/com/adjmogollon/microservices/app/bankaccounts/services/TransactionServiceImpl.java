package com.adjmogollon.microservices.app.bankaccounts.services;

import com.adjmogollon.microservices.app.bankaccounts.entity.Transaction;
import com.adjmogollon.microservices.app.bankaccounts.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<Transaction> findAll() {
        return (List<Transaction>) transactionRepository.findAll();
    }

    @Override
    public Transaction findById(Long id) {
        Optional<Transaction> result = transactionRepository.findById(id);
        return result.orElse(null);
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteById(Long id) {
        transactionRepository.deleteById(id);
    }
}