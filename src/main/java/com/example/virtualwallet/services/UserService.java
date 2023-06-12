package com.example.virtualwallet.services;

import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {

    void create(int id, User user, MultipartFile multipartFile) throws MessagingException, UnsupportedEncodingException;

    Page<User> getAll(Pageable pageable);

    Page<User> getAllUnidentified(Pageable pageable);

    User getById(int id);

    void update(int id, User user, MultipartFile multipartFile, MultipartFile multipartFileId);

    void delete(User user);

    void block(User user);

    void identify(User user, boolean approved);

    List<User>  getByUsername(String username);

    List<User>  getCurrentUser();

    void sendRegistrationCode(User user) throws MessagingException, UnsupportedEncodingException;

    List<Transaction> getTransaction(User user);

    Page<User> findUser(String search, Pageable pageable);

    Page<User> findRecipient(String search, Pageable pageable);

    void confirmCode(String code);
}
