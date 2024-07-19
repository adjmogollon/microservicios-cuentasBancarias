package com.adjmogollon.microservices.app.bankaccounts.repository;

import com.adjmogollon.microservices.app.bankaccounts.entity.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {
    
}