package com.example.ordervalidation.Client;

import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;


public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findClientByEmail(String email);
}
