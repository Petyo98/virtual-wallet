package com.example.virtualwallet.controllers.rest;

import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.exceptions.EntityNotFoundException;
import com.example.virtualwallet.models.Card;
import com.example.virtualwallet.services.CardService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/cards")
public class CardRestController {

    private CardService cardService;

    @Autowired
    public CardRestController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping()
    public Page<Card> getAll(Pageable pageable){
        return cardService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public Card getById(@PathVariable int id){
        try {
            return cardService.getById(id);
        }
        catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/create")
    public Card createCard(@RequestBody @Valid Card newCard){
        try {
            cardService.create(newCard);
            return newCard;
        }
        catch (DuplicateEntityException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

    }

    @PutMapping("/edit/{id}")
    public Card editCard(@PathVariable int id, @RequestBody @Valid Card newCard){
        try {
           cardService.update(id, newCard);
            return newCard;
        }
        catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/delete/{id}")
    public void deleteCard(@PathVariable int id) {
        try {
            Card card = cardService.getById(id);
            cardService.delete(card);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
