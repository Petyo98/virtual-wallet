package com.example.virtualwallet.services.impl;

import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.exceptions.InvalidCardInputException;
import com.example.virtualwallet.models.Card;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.models.Wallet;
import com.example.virtualwallet.repositories.CardRepository;
import com.example.virtualwallet.repositories.UserRepository;
import com.example.virtualwallet.repositories.WalletRepository;
import com.example.virtualwallet.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl implements CardService {
    private CardRepository cardRepository;
    private WalletRepository walletRepository;
    private UserRepository userRepository;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository, WalletRepository walletRepository, UserRepository userRepository) {
        this.cardRepository = cardRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void create(Card card) {
        if(cardRepository.existsByNumber(card.getNumber())) {
            throw new DuplicateEntityException(
                    String.format("Card with number %s already exists", card.getNumber()));
        }
        validateUserInput(card);
        cardRepository.save(card);
    }

    @Override
    public void addCard(int userId, Card card) {
        User user = userRepository.getById(userId);
        Wallet wallet = walletRepository.getByUserId(user.getId());
        Card card1 = new Card();
        card1.setCardHolderName(card.getCardHolderName());
        card1.setNumber(card.getNumber());
        card1.setCsv(card.getCsv());
        card1.setExpireDate(card.getExpireDate());
        card1.setWallet(wallet);
        validateUserInput(card1);
        cardRepository.save(card1);
        walletRepository.save(wallet);
    }


    @Override
    public void update(int id,Card card) {
        validateUserInput(card);
        Card newCard = cardRepository.getById(id);
        newCard.setNumber(card.getNumber());
        newCard.setCsv(card.getCsv());
        newCard.setExpireDate(card.getExpireDate());
        cardRepository.save(newCard);
    }

    @Override
    public void delete(Card card) {
        card.setDeleted(true);
        cardRepository.save(card);
    }

    @Override
    public Page<Card> getAll(Pageable pageable) {
        return cardRepository.findAll(pageable);
    }

    @Override
    public Page<Card> getAllByUserId(int userId, Pageable pageable) {
        Wallet wallet = walletRepository.getByUserId(userId);
        return cardRepository.findAllByWallet(wallet, pageable);
    }

    @Override
    public Card getById(int id) {
        return cardRepository.getById(id);
    }

    private void validateUserInput(Card card) {
        final int currentYear = 20;

        String str = card.getExpireDate().substring(2,3);
        int month =Integer.parseInt(card.getExpireDate().substring(0,2));
        int year =Integer.parseInt(card.getExpireDate().substring(3,5));

        if (card.getNumber().length() != 16) {
            throw new InvalidCardInputException("Invalid card number length");
        }
        if (card.getCsv().length() !=3) {
            throw new InvalidCardInputException("Invalid card CSV length");
        }
        if (month > 12 || month < 1) {
            throw new InvalidCardInputException("Invalid month");
        }
        if (year <= currentYear) {
            throw new InvalidCardInputException("Invalid year");
        }
        if(!str.equals("/")){
            throw new InvalidCardInputException("Wrong symbol");
        }
    }
}
