package com.example.virtualwallet.services;

import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.models.*;
import com.example.virtualwallet.repositories.TransactionRepository;
import com.example.virtualwallet.repositories.TransactionVerificationRepository;
import com.example.virtualwallet.repositories.UserRepository;
import com.example.virtualwallet.repositories.WalletRepository;
import com.example.virtualwallet.services.impl.EmailSenderService;
import com.example.virtualwallet.services.impl.WalletServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mock.web.MockMultipartFile;

import javax.mail.MessagingException;
import java.awt.event.WindowAdapter;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WalletServiceImplTests {

    @Mock
    WalletRepository walletRepository;

    @Mock
    TransactionRepository transactionRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    JavaMailSender javaMailSender;

    @Mock
    TransactionVerificationRepository transactionVerificationRepository;

    @InjectMocks
    WalletServiceImpl walletService;

    @Mock
    EmailSenderService emailSenderService;

    @Test
    public void getAllWalletTest(){

        Wallet wallet = new Wallet();
        Wallet wallet1 = new Wallet();

        List<Wallet> walletList = new ArrayList<>();
        walletList.add(wallet);
        walletList.add(wallet1);
        when(walletRepository.findAll()).thenReturn(walletList);

        List<Wallet> result = walletService.getAll();

        Assert.assertEquals(result,walletList);

    }

    @Test
    public void getByIdShould_ReturnTag_WhenWalletExists(){

        Wallet wallet = new Wallet();
        wallet.setId(1);

        when(walletRepository.getById(anyInt()))
                .thenReturn(wallet);

        Wallet result = walletService.getById(5);

        Assert.assertEquals(result.getId(),wallet.getId());
    }

    @Test
    public void updateWalletTest(){
        Wallet wallet = new Wallet();

        walletService.update(wallet);

        verify(walletRepository,times(1)).save(wallet);

    }

    @Test
    public void addFundsTest(){
        User user = new User();
        Card card = new Card();
        Wallet wallet = new Wallet();
        wallet.setUserId(user.getId());
        user.setWallet(wallet);
        user.getWallet().setAmount(user.getWallet().getAmount() + 20);
        Transaction transaction = new Transaction();
//        transaction.setSender(user);
//        transaction.setReceiver(user);
//        transaction.setCard(card);
//        transaction.setVerified(true);
//        transaction.setDate(LocalDateTime.now());
//        when(walletRepository.getByUserId(user.getId())).thenReturn(wallet);
//        when(transactionRepository.existsById(transaction.getId())).thenReturn(true);
//        transactio
        walletService.addFunds(user,card,20);
//        verify(transactionRepository,times(1)).save(transaction);
        verify(walletRepository,times(1)).save(wallet);
    }

    @Test
    public void sendMoney(){
        User sender = new User();
        Wallet wallet = new Wallet();
        wallet.setAmount(50);
        wallet.setUserId(sender.getId());
        sender.setWallet(wallet);
        User receiver = new User();
        Wallet receiverWallet = new Wallet();
        receiverWallet.setUserId(receiver.getId());
        receiver.setWallet(receiverWallet);
        sender.getWallet().setAmount(sender.getWallet().getAmount() - 20);
        receiver.getWallet().setAmount(receiver.getWallet().getAmount() + 20);
        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDate(LocalDateTime.now());
        transaction.setAmount(20);
        transaction.setCurrency("USD");
        walletService.sendMoney(sender,receiver,20);
        ArgumentCaptor<Transaction> cardCaptor = ArgumentCaptor.forClass(Transaction.class);

//        when(walletRepository.getById(wallet.getId())).thenReturn(wallet);
//        when(walletRepository.getById(receiverWallet.getId())).thenReturn(receiverWallet);
//        when(transactionRepository.existsById(transaction.getId())).thenReturn(true);
        verify(transactionRepository,times(1)).save(cardCaptor.capture());
        verify(walletRepository,times(1)).save(wallet);
        verify(walletRepository,times(1)).save(receiverWallet);
    }

    @Test
    public void getByUserIdTest(){
        User user = new User();
        Wallet wallet = new Wallet();
        wallet.setUserId(user.getId());
        user.setWallet(wallet);

        when(walletRepository.getByUserId(user.getId())).thenReturn(wallet);

        walletService.getByUserId(user.getId());

        verify(walletRepository,times(1)).getByUserId(wallet.getId());

    }

    @Test
    public void sendMessageTest() throws MessagingException, UnsupportedEncodingException {

       User user = new User();
       user.setEmail("test@abv.bg");
       Transaction transaction = new Transaction();

        walletService.sendMessage(transaction,user);


        verify(emailSenderService,times(1)).sendEmail(any());
    }

}
