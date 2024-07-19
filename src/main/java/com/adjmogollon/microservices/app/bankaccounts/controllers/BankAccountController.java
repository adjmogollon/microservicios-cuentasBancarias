package com.adjmogollon.microservices.app.bankaccounts.controllers;

import com.adjmogollon.microservices.app.bankaccounts.dto.BankAccountRequest;
import com.adjmogollon.microservices.app.bankaccounts.dto.BankAccountResponse;
import com.adjmogollon.microservices.app.bankaccounts.dto.BankAccountUpdateRequest;
import com.adjmogollon.microservices.app.bankaccounts.entity.BankAccount;
import com.adjmogollon.microservices.app.bankaccounts.entity.Client;
import com.adjmogollon.microservices.app.bankaccounts.services.BankAccountService;
import com.adjmogollon.microservices.app.bankaccounts.services.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/bank-accounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final ClientService clientService;

    public BankAccountController(BankAccountService bankAccountService, ClientService clientService) {
        this.bankAccountService = bankAccountService;
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<BankAccount>> getAllBankAccounts() {
        List<BankAccount> bankAccounts = bankAccountService.findAll();
        return ResponseEntity.ok(bankAccounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccount> getBankAccountById(@PathVariable Long id) {
        BankAccount bankAccount = bankAccountService.findById(id);
        if (bankAccount != null) {
            return ResponseEntity.ok(bankAccount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createBankAccount(@RequestBody BankAccountRequest bankAccountRequest) {

        // Verificar si el número de cuenta ya existe
        if (bankAccountService.existsByAccount(bankAccountRequest.getAccountNumber())) {
            return ResponseEntity.badRequest().body("El número de cuenta ya existe.");
        }

        // Crear un nuevo cliente
        Client newClient = new Client();
        newClient.setName(bankAccountRequest.getClientName());
        newClient.setEmail(bankAccountRequest.getClientEmail());
        newClient.setPhoneNumber(bankAccountRequest.getClientPhoneNumber());

        // Guardar el cliente
        Client savedClient = clientService.save(newClient);

        // Crear y guardar la nueva cuenta bancaria
        BankAccount bankAccount = new BankAccount();
        bankAccount.setClient(savedClient);
        bankAccount.setAccount(bankAccountRequest.getAccountNumber());
        bankAccount.setAmount(bankAccountRequest.getAmount());
        bankAccount.setStatus(true); // Activa por defecto
        bankAccount.setCreationDate(new Date());
        bankAccount.setUpdateDate(new Date());

        BankAccount savedBankAccount = bankAccountService.save(bankAccount);

        // Preparar la respuesta
        BankAccountResponse response = new BankAccountResponse();
        response.setId(savedBankAccount.getId());
        response.setAccount(savedBankAccount.getAccount());
        response.setAmount(savedBankAccount.getAmount());
        response.setStatus(savedBankAccount.isStatus());
        response.setCreationDate(savedBankAccount.getCreationDate());
        response.setUpdateDate(savedBankAccount.getUpdateDate());
        response.setClientName(savedClient.getName());
        response.setClientEmail(savedClient.getEmail());
        response.setClientPhoneNumber(savedClient.getPhoneNumber());

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{account}")
    public ResponseEntity<BankAccount> updateBankAccount(
            @PathVariable String account,
            @RequestBody BankAccountUpdateRequest bankAccountUpdateRequest) {

        BankAccount existingBankAccount = bankAccountService.findByAccount(account);
        if (existingBankAccount == null) {
            return ResponseEntity.notFound().build();
        }

        // Actualizar los campos deseados
        existingBankAccount.setAmount(bankAccountUpdateRequest.getAmount());
        existingBankAccount.setUpdateDate(new Date());

        BankAccount savedBankAccount = bankAccountService.save(existingBankAccount);
        return ResponseEntity.ok(savedBankAccount);
    }
/*
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable Long id) {
        BankAccount bankAccount = bankAccountService.findById(id);
        if (bankAccount == null) {
            return ResponseEntity.notFound().build();
        }

        bankAccountService.deactivateById(id);
        return ResponseEntity.noContent().build();
    }
*/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable Long id) {
        BankAccount bankAccount = bankAccountService.findById(id);
        if (bankAccount == null) {
            return ResponseEntity.notFound().build();
        }

        bankAccountService.deactivateById(id);
        return ResponseEntity.noContent().build();
    }
}
