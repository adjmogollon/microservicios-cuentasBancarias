package com.adjmogollon.microservices.app.bankaccounts.services;

import com.adjmogollon.microservices.app.bankaccounts.entity.BankAccount;
import com.adjmogollon.microservices.app.bankaccounts.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountServiceImpl implements BankAccountService {

	private static BankAccountRepository bankAccountRepository;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
        BankAccountServiceImpl.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public List<BankAccount> findAll() {
        return (List<BankAccount>) bankAccountRepository.findAll();
    }

    @Override
    public BankAccount findById(Long id) {
        Optional<BankAccount> result = bankAccountRepository.findById(id);
        return result.orElse(null);
    }

    @Override
    public BankAccount save(BankAccount bankAccount) {
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public void deleteById(Long id) {
        bankAccountRepository.deleteById(id);
    }
    
    @Override
    public void deactivateById(Long id) {
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(id);
        if (optionalBankAccount.isPresent()) {
            BankAccount bankAccount = optionalBankAccount.get();
            bankAccount.setStatus(false);
            bankAccountRepository.save(bankAccount);
        }
    }
    
    @Override
    public boolean existsByAccount(String accountNumber) {
        return bankAccountRepository.existsByAccount(accountNumber);
    }
    
    @Override
    public BankAccount findByAccount(String account) {
        return bankAccountRepository.findByAccount(account);
    }
}
