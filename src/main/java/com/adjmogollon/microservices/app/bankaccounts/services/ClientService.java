package com.adjmogollon.microservices.app.bankaccounts.services;

import com.adjmogollon.microservices.app.bankaccounts.entity.Client;
import java.util.List;

public interface ClientService {
    List<Client> findAll();
    Client findById(Long id);
    Client save(Client client);
    void deleteById(Long id);
}