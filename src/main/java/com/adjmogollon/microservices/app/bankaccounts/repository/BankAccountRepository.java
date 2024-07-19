package com.adjmogollon.microservices.app.bankaccounts.repository;

import com.adjmogollon.microservices.app.bankaccounts.entity.BankAccount;
import org.springframework.data.repository.CrudRepository;

public interface BankAccountRepository extends CrudRepository<BankAccount, Long> {
    boolean existsByAccount(String accountNumber);
    BankAccount findByAccount(String account);

}
