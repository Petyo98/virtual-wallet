package com.example.virtualwallet.services;

import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.models.Card;
import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.models.Wallet;
import com.example.virtualwallet.repositories.TransactionRepository;
import com.example.virtualwallet.services.impl.TransactionServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTests {

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionServiceImpl transactionService;


    @Test
    public void getAllTransactionsTest() {
        Transaction transaction = new Transaction();
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        Page<Transaction> transactions = new PageImpl(transactionList);
        Mockito.when(transactionRepository.findAll(Mockito.any(Pageable.class))).thenReturn(transactions);
        transactionService.getAll(Pageable.unpaged());
//        List<Card> userPersonalDataDtoList = cardService.getAll(Pageable.unpaged());
//        Assert.assertEquals(1, cardList.size());
        verify(transactionRepository,times(1)).findAll(Pageable.unpaged());

    }

    @Test
    public void getByIdShould_ReturnTag_WhenTransactionExists(){

        Transaction transaction = new Transaction();
        transaction.setId(1);

        when(transactionRepository.getById(1))
                .thenReturn(transaction);

        Transaction result = transactionService.getById(1);

        Assert.assertEquals(result.getId(),transaction.getId());
    }

    @Test
    public void createTransactionTest(){
        Transaction transaction = new Transaction();

        transactionService.create(transaction);

        verify(transactionRepository,times(1)).save(transaction);

    }

    @Test(expected = DuplicateEntityException.class)
    public void shouldThrowException_whenNumberIsDuplicated(){
        Transaction transaction = new Transaction();
        transaction.setId(1);
        when(transactionRepository.existsById(transaction.getId())).thenReturn(true);
        transactionService.create(transaction);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void findAllByUserTest() {
        User user = new User();
        Transaction transaction = new Transaction();
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        Page<Transaction> transactions = new PageImpl(transactionList);

        Mockito.when(transactionRepository.findAllBySenderOrReceiver(user,user, Pageable.unpaged()))
                .thenReturn(transactions);
        transactionService.findAllByUser(user,Pageable.unpaged());
        Assert.assertEquals(1, transactionList.size());
        verify(transactionRepository,times(1))
                .findAllBySenderOrReceiver(user,user,Pageable.unpaged());

    }

    @Test
    public void findAllByReceiverTest() {
        User user = new User();
        Transaction transaction = new Transaction();
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        Page<Transaction> transactions = new PageImpl(transactionList);

        Mockito.when(transactionRepository.findAllByReceiver(user, Pageable.unpaged()))
                .thenReturn(transactions);
        transactionService.findAllByReceiver(user,Pageable.unpaged());
        Assert.assertEquals(1, transactionList.size());
        verify(transactionRepository,times(1))
                .findAllByReceiver(user,Pageable.unpaged());

    }

    @Test
    public void findAllBySenderTest() {
        User user = new User();
        Transaction transaction = new Transaction();
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        Page<Transaction> transactions = new PageImpl(transactionList);

        Mockito.when(transactionRepository.findAllBySender(user, Pageable.unpaged()))
                .thenReturn(transactions);
        transactionService.findAllBySender(user,Pageable.unpaged());
        Assert.assertEquals(1, transactionList.size());
        verify(transactionRepository,times(1))
                .findAllBySender(user,Pageable.unpaged());

    }

    @Test
    public void findBetweenDatesTest() {
        User user = new User();
        Transaction transaction = new Transaction();
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        Page<Transaction> transactions = new PageImpl(transactionList);
        LocalDateTime start = LocalDateTime.parse("2020-12-12T11:11");
        LocalDateTime end =  LocalDateTime.parse("2021-12-12T11:11");
        Mockito.when(transactionRepository.findAllByDateBetween(start,end, Pageable.unpaged()))
                .thenReturn(transactions);
        transactionService.findAllByDateBetween(start,end,Pageable.unpaged());
        Assert.assertEquals(1, transactionList.size());
        verify(transactionRepository,times(1))
                .findAllByDateBetween(start,end,Pageable.unpaged());

    }

    @Test
    public void findAllBySenderOrReceiverTest() {
        User user = new User();
        Transaction transaction = new Transaction();
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        Page<Transaction> transactions = new PageImpl(transactionList);
        LocalDateTime start = LocalDateTime.parse("2020-12-12T11:11");
        LocalDateTime end =  LocalDateTime.parse("2021-12-12T11:11");
        Mockito.when(transactionRepository
                .findAllByDateBetweenAndSenderOrReceiver(start,end,user,user, Pageable.unpaged()))
                .thenReturn(transactions);
        transactionService.findAllByDateBetween(start,end,user,Pageable.unpaged());
        Assert.assertEquals(1, transactionList.size());
        verify(transactionRepository,times(1))
                .findAllByDateBetweenAndSenderOrReceiver(start,end,user,user,Pageable.unpaged());

    }
}
