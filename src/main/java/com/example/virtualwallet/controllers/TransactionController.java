package com.example.virtualwallet.controllers;

import com.example.virtualwallet.exceptions.EntityNotFoundException;
import com.example.virtualwallet.models.*;
import com.example.virtualwallet.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TransactionController {

    private UserService userService;
    private WalletService walletService;
    private CardService cardService;


    @Autowired
    public TransactionController(UserService userService,
                                 WalletService walletService,
                                 CardService cardService){
        this.userService = userService;
        this.walletService = walletService;
        this.cardService = cardService;
    }

    @PostMapping("/transaction/search")
    public String searchUser(Model model, @RequestParam("search") String search, Pageable pageable){
        Page<User> userList = userService.findUser(search, pageable);
        model.addAttribute("userList", userList);
        return "show-result";
    }

    @GetMapping("/create/transaction/{userId}")
    public String showFormCreateTransaction(Model model, @PathVariable("userId") int userId){
        User user= userService.getCurrentUser().get(0);
        User receiver = userService.getById(userId);
        Wallet wallet = user.getWallet();
        List<Card> cardList = new ArrayList<>(wallet.getCardList());
        model.addAttribute("wallet",wallet);
        model.addAttribute("cardList",cardList);
        model.addAttribute("receiver",receiver);
        return "redirect:/user";
    }

    @PostMapping("make/transaction")
    public String makeTransaction(){
        return "redirect:/user";
    }


    @PostMapping("/add/money/to/wallet")
    public String addMoneyToWallet(Model model,
                                   @RequestParam("amount") double amount,
                                   @RequestParam(defaultValue = "0") int  cardId,
                                   @RequestParam("description") String description
                                   ){

        User user = userService.getCurrentUser().get(0);
        Wallet wallet = user.getWallet();
        Card newCard = cardService.getById(cardId);
        try{
            HttpStatus responseEntity = walletService.sendRequestApi(newCard, user, amount, description);
            if(responseEntity == HttpStatus.OK || responseEntity.is3xxRedirection()){
                walletService.addFunds(user,newCard,amount);
            }
        }
        catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,e.getMessage());
        }
        catch (HttpClientErrorException.Forbidden e){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't have enough money in your card");
        }
        catch (HttpClientErrorException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,e.getMessage());
        }
        List<Card> cardList = new ArrayList<>(wallet.getCardList());
        model.addAttribute("cardList", cardList);
        model.addAttribute("wallet", wallet);

        return "redirect:/user";
    }


    @PostMapping("/send-money/{userId}")
    public String transferMoney(Model model, double amount, @PathVariable ("userId") int userId) throws MessagingException, UnsupportedEncodingException {
        User user = userService.getCurrentUser().get(0);
        User receiver = userService.getById(userId);
        if(amount >= 2000){
            walletService.sendMoney(user,receiver,amount);
            return "transaction-confirmation";
        }
        walletService.sendMoney(user,receiver,amount);
        return "redirect:/user";
    }

    @PostMapping("/verified")
    public String verifiedTransaction(@RequestParam("code") String code){
        User user = userService.getCurrentUser().get(0);
        walletService.confirmCode(user, code);
        return "redirect:/user";
    }

}
