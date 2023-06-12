package com.example.virtualwallet.repositories;

import com.example.virtualwallet.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    Wallet getById(int id);

    Wallet getByUserId(int id);
}
