package com.adjmogollon.microservicios.app.bankaccounts.controllers;
import com.adjmogollon.microservices.app.bankaccounts.controllers.BankAccountController;
import com.adjmogollon.microservices.app.bankaccounts.dto.BankAccountRequest;
import com.adjmogollon.microservices.app.bankaccounts.dto.BankAccountResponse;
import com.adjmogollon.microservices.app.bankaccounts.dto.BankAccountUpdateRequest;
import com.adjmogollon.microservices.app.bankaccounts.entity.BankAccount;
import com.adjmogollon.microservices.app.bankaccounts.entity.Client;
import com.adjmogollon.microservices.app.bankaccounts.services.BankAccountService;
import com.adjmogollon.microservices.app.bankaccounts.services.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BankAccountControllerTest {

	
    @Mock
    private BankAccountService bankAccountService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private BankAccountController bankAccountController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBankAccounts() {
        BankAccount bankAccount1 = new BankAccount();
        BankAccount bankAccount2 = new BankAccount();
        List<BankAccount> bankAccounts = Arrays.asList(bankAccount1, bankAccount2);

        when(bankAccountService.findAll()).thenReturn(bankAccounts);

        ResponseEntity<List<BankAccount>> response = bankAccountController.getAllBankAccounts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bankAccounts, response.getBody());
    }

    @Test
    void testGetBankAccountById() {
        Long id = 1L;
        BankAccount bankAccount = new BankAccount();
        when(bankAccountService.findById(id)).thenReturn(bankAccount);

        ResponseEntity<BankAccount> response = bankAccountController.getBankAccountById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bankAccount, response.getBody());
    }

    @Test
    void testGetBankAccountById_NotFound() {
        Long id = 1L;
        when(bankAccountService.findById(id)).thenReturn(null);

        ResponseEntity<BankAccount> response = bankAccountController.getBankAccountById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateBankAccount() {
        BankAccountRequest request = new BankAccountRequest();
        request.setClientName("Jesus Mogollon");
        request.setClientEmail("jmogollon@gmail.com");
        request.setClientPhoneNumber("1234567890");
        request.setAccountNumber("12345");
        request.setAmount(100.0);

        Client client = new Client();
        client.setName("Jesus Mogollon");
        client.setEmail("jmogollon@gmail.com");
        client.setPhoneNumber("1234567890");

        BankAccount bankAccount = new BankAccount();
        bankAccount.setClient(client);
        bankAccount.setAccount("123456");
        bankAccount.setAmount(100.0);
        bankAccount.setStatus(true);
        bankAccount.setCreationDate(new Date());
        bankAccount.setUpdateDate(new Date());

        when(clientService.save(any(Client.class))).thenReturn(client);
        when(bankAccountService.save(any(BankAccount.class))).thenReturn(bankAccount);

        ResponseEntity<?> response = bankAccountController.createBankAccount(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof BankAccountResponse);
        BankAccountResponse responseBody = (BankAccountResponse) response.getBody();
        assertEquals("123456", responseBody.getAccount());
        assertEquals("Jesus Mogollon", responseBody.getClientName());
    }

    @Test
    void testCreateBankAccount_AccountExists() {
        BankAccountRequest request = new BankAccountRequest();
        request.setAccountNumber("12345");

        when(bankAccountService.existsByAccount("12345")).thenReturn(true);

        ResponseEntity<?> response = bankAccountController.createBankAccount(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El número de cuenta ya existe.", response.getBody());
    }

    @Test
    void testUpdateBankAccount() {
        // Datos de prueba
        String accountNumber = "3105";
        BankAccountUpdateRequest request = new BankAccountUpdateRequest();
        request.setAmount(100.00);

        // Cuenta existente
        BankAccount existingBankAccount = new BankAccount();
        existingBankAccount.setId(1L);
        existingBankAccount.setAccount(accountNumber);
        existingBankAccount.setAmount(50.20);
        existingBankAccount.setStatus(true);
        existingBankAccount.setCreationDate(new Date());
        existingBankAccount.setUpdateDate(new Date());
        existingBankAccount.setClient(new Client());

        // Mockear comportamiento
        when(bankAccountService.findByAccount(anyString())).thenReturn(existingBankAccount);
        when(bankAccountService.save(any(BankAccount.class))).thenReturn(existingBankAccount);

        // Llamar al método del controlador
        ResponseEntity<BankAccount> response = bankAccountController.updateBankAccount(accountNumber, request);

        // Verificaciones
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(100.00, response.getBody().getAmount());
        verify(bankAccountService, times(1)).findByAccount(accountNumber);
        verify(bankAccountService, times(1)).save(any(BankAccount.class));
    }

    
    @Test
    void testDeleteBankAccount() {
        Long id = 1L;
        BankAccount bankAccount = new BankAccount();

        when(bankAccountService.findById(id)).thenReturn(bankAccount);
        doNothing().when(bankAccountService).deactivateById(id);

        ResponseEntity<Void> response = bankAccountController.deleteBankAccount(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bankAccountService, times(1)).deactivateById(id);
    }

    @Test
    void testDeleteBankAccount_NotFound() {
        Long id = 1L;

        when(bankAccountService.findById(id)).thenReturn(null);

        ResponseEntity<Void> response = bankAccountController.deleteBankAccount(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
/*
    @Test
    void testDeactivateBankAccount() {
        Long id = 1L;
        BankAccount bankAccount = new BankAccount();

        when(bankAccountService.findById(id)).thenReturn(bankAccount);
        doNothing().when(bankAccountService).deactivateById(id);

        ResponseEntity<Void> response = bankAccountController.deactivateBankAccount(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bankAccountService, times(1)).deactivateById(id);
    }

    @Test
    void testDeactivateBankAccount_NotFound() {
        Long id = 1L;

        when(bankAccountService.findById(id)).thenReturn(null);

        ResponseEntity<Void> response = bankAccountController.deactivateBankAccount(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }*/
}