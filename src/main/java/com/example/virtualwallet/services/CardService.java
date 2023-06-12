package com.example.virtualwallet.services;

import com.example.virtualwallet.models.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardService {

    void create(Card card);

    void update(int id ,Card card);

    void delete(Card card);

    Page<Card> getAll(Pageable pageable);

    Page<Card> getAllByUserId(int userId, Pageable pageable);

    Card getById(int id);

    void addCard(int id, Card card);

}
