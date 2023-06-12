package com.example.virtualwallet.services;

import com.example.virtualwallet.models.*;
import org.springframework.http.HttpStatus;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface WalletService {

    void update(Wallet wallet);

    List<Wallet> getAll();

    Wallet getById(int id);

    Wallet getByUserId(int id);

    void addFunds(User user,Card card, double amount);

    void sendMoney(User sender, User receiver, double amount) throws MessagingException, UnsupportedEncodingException;

    void sendMessage(Transaction transaction, User sender) throws MessagingException, UnsupportedEncodingException;

    void confirmCode(User user, String code);

     HttpStatus sendRequestApi(Card card, User user, double amount, String description);
}
