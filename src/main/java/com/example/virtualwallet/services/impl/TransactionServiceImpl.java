package com.example.virtualwallet.services.impl;

import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.repositories.TransactionRepository;
import com.example.virtualwallet.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {
    private TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void create(Transaction transaction) {
        if(transactionRepository.existsById(transaction.getId())) {
            throw new DuplicateEntityException(
                    String.format("Transaction with ID %s already exists", transaction.getId()));
        }
        transactionRepository.save(transaction);
    }

    @Override
    public Page<Transaction> getAll(Pageable pageable) {
        return transactionRepository.findAll(pageable);
    }

    @Override
    public Transaction getById(int id) {
        return transactionRepository.getById(id);
    }

    @Override
    public Page<Transaction> findAllByUser(User user, Pageable pageable) {
        return transactionRepository.findAllBySenderOrReceiver(user, user, pageable);
    }

    @Override
    public Page<Transaction> findAllByReceiver(User user, Pageable pageable) {
        return transactionRepository.findAllByReceiver(user, pageable);
    }

    @Override
    public Page<Transaction> findAllBySender(User user, Pageable pageable) {
        return transactionRepository.findAllBySender(user, pageable);
    }

    @Override
    public Page<Transaction> findAllByDateBetween(LocalDateTime start, LocalDateTime end, User user, Pageable pageable) {
        return transactionRepository.findAllByDateBetweenAndSenderOrReceiver(start, end, user, user, pageable);
    }

    @Override
    public Page<Transaction> findAllByDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return transactionRepository.findAllByDateBetween(start, end, pageable);
    }
}
