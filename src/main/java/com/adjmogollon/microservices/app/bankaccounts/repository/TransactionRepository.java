package com.adjmogollon.microservices.app.bankaccounts.repository;


import com.adjmogollon.microservices.app.bankaccounts.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

}
