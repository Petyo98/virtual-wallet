package com.example.virtualwallet.services;

import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface TransactionService {

    void create(Transaction transaction);

    Page<Transaction> getAll(Pageable pageable);

    Transaction getById(int id);

    Page<Transaction> findAllByUser(User user, Pageable pageable);

    Page<Transaction> findAllByReceiver(User user, Pageable pageable);

    Page<Transaction> findAllBySender(User user, Pageable pageable);

    Page<Transaction> findAllByDateBetween(LocalDateTime start, LocalDateTime end, User user, Pageable page);

    Page<Transaction> findAllByDateBetween(LocalDateTime start, LocalDateTime end, Pageable page);

}
