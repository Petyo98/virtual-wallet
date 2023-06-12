package com.example.virtualwallet.controllers;

import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.services.TransactionService;
import com.example.virtualwallet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class AdminController {
    private UserService userService;
    private TransactionService transactionService;

    @Autowired
    public AdminController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @GetMapping("/admin")
    public String showAdmin() {
        return "admin";
    }


    @GetMapping("admin/users")
    public String showUser(Model model, Pageable pageable) {
        Page<User> userList = userService.getAll(
                pageable = PageRequest.of(pageable.getPageNumber(),10));
        model.addAttribute("users", userList);
        model.addAttribute("totalPages", userList.getTotalPages());
        return "admin-users";
    }

    @PostMapping("admin/users/search")
    public String searchUsers(Model model, @RequestParam("search") String search, Pageable pageable) {
        Page<User> userList = userService.findUser(
                search, pageable = PageRequest.of(pageable.getPageNumber(),10));
        model.addAttribute("userList", userList);
        model.addAttribute("totalPages", userList.getTotalPages());

        return "admin-users-results";
    }

    @GetMapping("admin/transactions")
    public String showTransactions(Model model, Pageable pageable) {
        Page<Transaction> transactionList = transactionService.getAll(
                pageable = PageRequest.of(pageable.getPageNumber(),10, Sort.by("date").ascending()));
        model.addAttribute("transactions", transactionList);
        model.addAttribute("totalPages", transactionList.getTotalPages());
        return "admin-transactions";
    }

    @PostMapping("admin/users/block/{userName}")
    public String blockUser(@PathVariable String userName) {
        userService.block(userService.getByUsername(userName).get(0));
        return "redirect:/admin/users";
    }

    @GetMapping("admin/identities")
    public String showIdentities(Model model, Pageable pageable) {
        Page<User> userList = userService.getAllUnidentified(
                pageable = PageRequest.of(pageable.getPageNumber(), 10));
        model.addAttribute("userList", userList);
        model.addAttribute("totalPages", userList.getTotalPages());
        return "admin-identities";
    }

    @PostMapping("admin/identities/approve/{userName}/{approved}")
    public String acceptIdentity(@PathVariable String userName, @PathVariable boolean approved) {
        userService.identify(userService.getByUsername(userName).get(0), approved);
        return "redirect:/admin/identities";
    }

    @GetMapping("/admin/transactions/amount")
    public String showTransactionsByAmount(Model model, Pageable pageable) {
        Page<Transaction> transactionList = transactionService.getAll(
                pageable = PageRequest.of(pageable.getPageNumber(),10, Sort.by("amount").ascending()));
        model.addAttribute("transactions", transactionList);
        model.addAttribute("totalPages", transactionList.getTotalPages());
        return "admin-transactions";
    }

    @PostMapping("/admin-sender-results")
    public String showSenderTransactionsResult(Model model, @RequestParam("searchSender") String search, Pageable pageable){
        Page<Transaction> transactionList = transactionService.findAllBySender(userService.getByUsername(search).get(0),
                pageable = PageRequest.of(pageable.getPageNumber(),10, Sort.by("date").ascending()));
        model.addAttribute("transactionList", transactionList);
        model.addAttribute("totalPages", transactionList.getTotalPages());
        return "admin-transactions-results";
    }

    @PostMapping("/admin-recipient-results")
    public String showRecipientTransactionsResult(Model model, @RequestParam("searchRecipient") String search, Pageable pageable){
        Page<Transaction> transactionList = transactionService.findAllByReceiver(userService.getByUsername(search).get(0),
                pageable = PageRequest.of(pageable.getPageNumber(),10, Sort.by("date").ascending()));
        model.addAttribute("transactionList", transactionList);
        model.addAttribute("totalPages", transactionList.getTotalPages());
        return "admin-transactions-results";
    }

    @GetMapping("/get/admin/transaction/by/period")
    public String getAdminTransactionFromDateToDate(Model model,@RequestParam("start") String start,
                                               @RequestParam("end") String end, Pageable pageable) {
        LocalDateTime start1 = LocalDateTime.parse(start);
        LocalDateTime end1 = LocalDateTime.parse(end);
        Page<Transaction> transactionList = transactionService.findAllByDateBetween(start1, end1,
                pageable = PageRequest.of(pageable.getPageNumber(),10, Sort.by("date").ascending()));
        model.addAttribute("transactionList",transactionList);
        model.addAttribute("totalPages", transactionList.getTotalPages());
        return "admin-transactions-results";
    }

}