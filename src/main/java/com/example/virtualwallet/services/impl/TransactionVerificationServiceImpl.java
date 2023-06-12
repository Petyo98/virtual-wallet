package com.example.virtualwallet.services.impl;

import com.example.virtualwallet.models.TransactionVerification;
import com.example.virtualwallet.repositories.TransactionVerificationRepository;
import com.example.virtualwallet.services.TransactionVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionVerificationServiceImpl implements TransactionVerificationService {
    private TransactionVerificationRepository transactionVerificationRepository;

    @Autowired
    public TransactionVerificationServiceImpl(TransactionVerificationRepository transactionVerificationRepository) {
        this.transactionVerificationRepository = transactionVerificationRepository;
    }

    @Override
    public void create(TransactionVerification transactionVerification) {
        transactionVerificationRepository.save(transactionVerification);
    }

    @Override
    public void update(TransactionVerification transactionVerification) {
        transactionVerificationRepository.save(transactionVerification);
    }

    @Override
    public TransactionVerification getById(int id) {
        return transactionVerificationRepository.getById(id);
    }
}
