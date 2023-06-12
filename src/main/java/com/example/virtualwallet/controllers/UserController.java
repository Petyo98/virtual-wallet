package com.example.virtualwallet.controllers;

import com.example.virtualwallet.models.Card;
import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.models.Wallet;
import com.example.virtualwallet.services.CardService;
import com.example.virtualwallet.services.TransactionService;
import com.example.virtualwallet.services.UserService;
import com.example.virtualwallet.services.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private UserService userService;
    private WalletService walletService;
    private CardService cardService;
    private TransactionService transactionService;

    @Autowired
    public UserController(UserService userService, WalletService walletService, CardService cardService,
                          TransactionService transactionService) {
        this.walletService = walletService;
        this.userService = userService;
        this.cardService = cardService;
        this.transactionService = transactionService;
    }

    @PostMapping("/user/details")
    public String updateUser(Model model,
                             @ModelAttribute("user") User newUser,
                             @RequestParam("file") MultipartFile multipart,
                             @RequestParam("fileId") MultipartFile multipartId
                            ) {
        User user = userService.getByUsername(userService.getCurrentUser().get(0).getUsername()).get(0);
        userService.update(user.getId(),newUser, multipart, multipartId);
        model.addAttribute("user", user);
        return "user-details";
    }

    @PostMapping("/user/add-card")
    public String addCard(@Valid @ModelAttribute("card")Card card, Model model){
        User user = userService.getCurrentUser().get(0);
        Wallet wallet = walletService.getByUserId(user.getId());
        List<Card> cardList = new ArrayList<>(wallet.getCardList());
        model.addAttribute("cardList", cardList);
        model.addAttribute("wallet", wallet);
        cardService.addCard(user.getId(),card);

        return "redirect:/user";
    }

    @GetMapping("/user")
    public String showUser(Model model, Pageable pageable) {
        User user =userService.getCurrentUser().get(0);
        Wallet wallet = walletService.getByUserId(user.getId());
        Page<Card> cardList = cardService.getAllByUserId(user.getId(),
                pageable = PageRequest.of(pageable.getPageNumber(),5));
        model.addAttribute("user",user);
        model.addAttribute("cardList", cardList);
        model.addAttribute("totalPages", cardList.getTotalPages());
        model.addAttribute("wallet", wallet);
        return "user";
    }

    @PostMapping("/card/delete")
    public String deleteCard(@Valid @ModelAttribute("card")Card card, Model model){
        Card card1 = cardService.getById(card.getId());
        cardService.delete(card1);
        User user = userService.getCurrentUser().get(0);
        Wallet wallet = walletService.getByUserId(user.getId());
        List<Card> cardList = new ArrayList<>(wallet.getCardList());
        model.addAttribute("cardList", cardList);
        model.addAttribute("wallet", wallet);
        return "redirect:/user";
    }

    @GetMapping("/card/edit-show/{cardId}")
    public String showEditTmpl(Model model,@PathVariable("cardId") int cardId){
        Card card = cardService.getById(cardId);
        model.addAttribute("card", card);
        return "edit-card";
    }

    @GetMapping("/user/transaction/history")
    public String showTransactionHistoryByDate(Model model, Pageable pageable){
        User user = userService.getCurrentUser().get(0);
        Page<Transaction> transactionList = transactionService.findAllByUser(user,
                pageable = PageRequest.of(pageable.getPageNumber(),5, Sort.by("date").ascending()));
        model.addAttribute("transactionList", transactionList);
        model.addAttribute("totalPages", transactionList.getTotalPages());
        return "transaction-history";
    }

    @GetMapping("/user/transaction/history/amount")
    public String showTransactionHistoryByAmount(Model model, Pageable pageable){
        User user = userService.getCurrentUser().get(0);
        Page<Transaction> transactionList = transactionService.findAllByUser(user,
                pageable = PageRequest.of(pageable.getPageNumber(),5, Sort.by("amount").ascending()));
        model.addAttribute("transactionList", transactionList);
        model.addAttribute("totalPages", transactionList.getTotalPages());
        return "transaction-history";
    }

    @GetMapping("/user/sender/history")
    public String showSenderHistory(Model model, Pageable pageable){
        User user = userService.getCurrentUser().get(0);
        Page<Transaction> transactionList = transactionService.findAllBySender(user,
                pageable = PageRequest.of(pageable.getPageNumber(),5, Sort.by("date").ascending()));
        model.addAttribute("transactionList", transactionList);
        model.addAttribute("totalPages", transactionList.getTotalPages());
        return "transaction-history";
    }

    @GetMapping("/user/receiver/history")
    public String showReceiverHistory(Model model, Pageable pageable){
        User user = userService.getCurrentUser().get(0);
        Page<Transaction> transactionList = transactionService.findAllByReceiver(user,
                pageable = PageRequest.of(pageable.getPageNumber(),5, Sort.by("date").ascending()));
        model.addAttribute("transactionList", transactionList);
        model.addAttribute("totalPages", transactionList.getTotalPages());
        return "transaction-history";
    }

    @PostMapping("/recipient-result")
    public String showRecipientResult(Model model, @RequestParam("search") String search, Pageable pageable){
        Page<User> userList = userService.findRecipient(search, pageable);
        Page<Transaction> transactionList = transactionService.findAllByReceiver(userList.getContent().get(0),
                pageable = PageRequest.of(pageable.getPageNumber(),5));
        model.addAttribute("transactionList", transactionList);
        model.addAttribute("totalPages", transactionList.getTotalPages());
        return "recipient-result";
    }

    @PostMapping("/card/edit/{cardId}")
    public String editCard(@ModelAttribute("card")Card card,@PathVariable("cardId") int cardId, Model model){
        Card newCard = cardService.getById(cardId);
        cardService.update(cardId,card);
        User user = userService.getCurrentUser().get(0);
        Wallet wallet = walletService.getByUserId(user.getId());
        List<Card> cardList = new ArrayList<>(wallet.getCardList());
        model.addAttribute("cardList", cardList);
        model.addAttribute("wallet", wallet);
        model.addAttribute("card",newCard);
        return "redirect:/user";
    }


    @GetMapping("/get/transaction/by/period")
    public String getTransactionFromDateToDate(Model model,@RequestParam("start") String start,
                                               @RequestParam("end") String end,
                                               Pageable pageable) {
        User user = userService.getCurrentUser().get(0);
        LocalDateTime start1 = LocalDateTime.parse(start);
        LocalDateTime end1 = LocalDateTime.parse(end);
        Page<Transaction> transactionList = transactionService.findAllByDateBetween(start1, end1, user,
                pageable = PageRequest.of(pageable.getPageNumber(),5, Sort.by("date").ascending()));
        model.addAttribute("transactionList",transactionList);
        model.addAttribute("totalPages", transactionList.getTotalPages());
        return "date-result";
    }
}
