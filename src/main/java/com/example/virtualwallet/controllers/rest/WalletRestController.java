package com.example.virtualwallet.controllers.rest;

import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.exceptions.EntityNotFoundException;
import com.example.virtualwallet.models.Wallet;
import com.example.virtualwallet.services.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;

@RestController
@RequestMapping("/api/wallets")
public class WalletRestController {

    private WalletService walletService;

    @Autowired
    public WalletRestController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping()
    public List<Wallet> getAll(){
        return walletService.getAll();
    }

    @GetMapping("/{id}")
    public Wallet getById(@PathVariable int id){
        try {
            return walletService.getById(id);
        }
        catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/edit/{id}")
    public Wallet editWallet(@PathVariable int id, @RequestBody @Valid Wallet newWallet){
        try {
            walletService.update(newWallet);
            return newWallet;
        }
        catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
