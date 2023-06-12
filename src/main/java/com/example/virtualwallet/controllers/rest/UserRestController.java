package com.example.virtualwallet.controllers.rest;

import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.exceptions.EntityNotFoundException;
import com.example.virtualwallet.models.Card;
import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.services.CardService;
import com.example.virtualwallet.services.TransactionService;
import com.example.virtualwallet.services.UserService;
import com.example.virtualwallet.services.UserVerificationService;
import com.example.virtualwallet.services.impl.EmailSenderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private UserService userService;
    private CardService cardService;


    @Autowired
    public UserRestController(UserService userService,
                              CardService cardService) {
        this.userService = userService;
        this.cardService = cardService;
    }

    @GetMapping()
    public Page<User> getAll(Pageable pageable){
        return userService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id){
        try {
            return userService.getById(id);
        }
        catch (EntityNotFoundException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/create")
    public User createUser(@ModelAttribute @Valid User user, @RequestParam MultipartFile multipartFile) {

        try {
            userService.create(user.getId(),user,multipartFile);
            return user;
        }
        catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    @PutMapping("/edit/{id}")
    public User editUser(@PathVariable int id, @RequestBody @Valid User newUser, MultipartFile multipartFile, MultipartFile multipartFileId){
        try {
            userService.update(id, newUser, multipartFile, multipartFileId);
            return newUser;
        }
        catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/delete/{id}")
    public void deleteUser(@PathVariable int id) {
        try {
            User user = userService.getById(id);

            user.setEnabled(false);

            userService.delete(user);

        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/add-card/{userId}/{cardId}")
    public void addCard(@PathVariable int userId,@PathVariable Card card){
        cardService.addCard(userId,card);
    }


    @PutMapping("/edit-card/{cardId}")
    public void editCard(@PathVariable int cardId){
        Card card = cardService.getById(cardId);
        cardService.update(cardId, card);
    }

    @PutMapping("/delete-card/{cardId}")
    public void deleteCard(@PathVariable int cardId){
        Card card = cardService.getById(cardId);
        cardService.delete(card);
    }

    @GetMapping("/user/transaction/{id}")
    public List<Transaction> showTransaction(@PathVariable int id){
        User user =userService.getById(id);
        return userService.getTransaction(user);

    }
}
