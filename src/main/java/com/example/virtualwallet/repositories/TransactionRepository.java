package com.example.virtualwallet.repositories;


import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    Transaction getById(int id);

    Page<Transaction> findAllBySenderOrReceiver(User sender, User receiver, Pageable pageable);

    Page<Transaction> findAllByReceiver(User receiver, Pageable pageable);

    Page<Transaction> findAllBySender(User sender, Pageable pageable);

    Page<Transaction> findAllByDateBetweenAndSenderOrReceiver(LocalDateTime start, LocalDateTime end, User sender, User receiver, Pageable pageable);

    Page<Transaction> findAllByDateBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    boolean existsById(int id);

    Page<Transaction> findAll(Pageable pageable);
}
