package com.example.virtualwallet.services;

import com.example.virtualwallet.models.TransactionVerification;

public interface TransactionVerificationService {

    void create(TransactionVerification transactionVerification);

    void update(TransactionVerification transactionVerification);

    TransactionVerification getById(int id);

}
