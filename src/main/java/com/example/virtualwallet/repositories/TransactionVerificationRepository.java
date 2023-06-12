package com.example.virtualwallet.repositories;

import com.example.virtualwallet.models.TransactionVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionVerificationRepository extends JpaRepository<TransactionVerification, Integer> {

    TransactionVerification getById(int id);
}
