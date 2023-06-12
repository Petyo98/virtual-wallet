package com.example.virtualwallet.repositories;

import com.example.virtualwallet.models.Card;
import com.example.virtualwallet.models.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {

    Card getById(int id);

    boolean existsByNumber(String number);

    Page<Card> findAll(Pageable pageable);

    Page<Card> findAllByWallet(Wallet wallet, Pageable pageable);
    
}
