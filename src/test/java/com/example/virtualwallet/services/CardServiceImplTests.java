package com.example.virtualwallet.services;

import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.exceptions.InvalidCardInputException;
import com.example.virtualwallet.models.*;
import com.example.virtualwallet.repositories.CardRepository;
import com.example.virtualwallet.repositories.UserRepository;
import com.example.virtualwallet.repositories.WalletRepository;
import com.example.virtualwallet.services.impl.CardServiceImpl;
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
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CardServiceImplTests {

    @Mock
    CardRepository cardRepository;

    @Mock
    WalletRepository walletRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    CardServiceImpl cardService;

    @Test(expected = DuplicateEntityException.class)
    public void shouldThrowException_whenNumberIsDuplicated(){
        Card card = new Card();
        card.setNumber("1234567890123456");
        when(cardRepository.existsByNumber(card.getNumber())).thenReturn(true);
        cardService.create(card);
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    public void getByIdShould_ReturnTag_WhenCardExists(){

        Card card = new Card();
        card.setId(1);

        when(cardRepository.getById(1)).thenReturn(card);

        Card result = cardService.getById(1);

        Assert.assertEquals(result.getId(),card.getId());
    }

    @Test
    public void updateCardTest(){
        Card card = new Card();
        card.setId(1);
        card.setNumber("1234567890123123");
        card.setCsv("123");
        card.setExpireDate("12/22");
        when(cardRepository.getById(card.getId())).thenReturn(card);
        cardService.update(card.getId(),card);
        verify(cardRepository,times(1)).save(card);

    }

    @Test
    public void deleteCardTest(){
        Card card = new Card();
        card.setDeleted(true);
        cardService.delete(card);
        verify(cardRepository,times(1)).save(card);

    }

    @Test
    public void createCardTest(){
        Card card = new Card();
        card.setCardHolderName("Petyo");
        card.setNumber("1234567890123456");
        card.setCsv("123");
        card.setExpireDate("12/22");
        cardService.create(card);

        verify(cardRepository,times(1)).save(card);

    }

    @Test
    public void getAllCardsTest() {
        Card card = new Card();
        List<Card> cardList = new ArrayList<>();
        cardList.add(card);
        Page<Card> cards = new PageImpl(cardList);
        Mockito.when(cardRepository.findAll(Mockito.any(Pageable.class))).thenReturn(cards);
        cardService.getAll(Pageable.unpaged());
//        List<Card> userPersonalDataDtoList = cardService.getAll(Pageable.unpaged());
//        Assert.assertEquals(1, cardList.size());
        verify(cardRepository,times(1)).findAll(Pageable.unpaged());

    }

//    @Test
//    public void getAllCardsByUserIdTest() {
//        Wallet wallet = new Wallet();
//        wallet.setUserId(1);
//        Card card = new Card();
//        List<Card> cardList = new ArrayList<>();
//        cardList.add(card);
//        wallet.setCardList(cardList);
//        Page<Card> cards = new PageImpl(cardList);
//        when(cardRepository.findAllByWallet(Mockito.any(Wallet.class), Mockito.any(Pageable.class))).thenReturn(cards);
//        cardService.getAllByUserId(1, Pageable.unpaged());
////        List<Card> userPersonalDataDtoList = cardService.getAll(Pageable.unpaged());
////        Assert.assertEquals(1, cardList.size());
//        verify(cardRepository,times(1)).findAllByWallet(wallet, Pageable.unpaged());
//
//    }

    @Test
    public void getAllCardsByUserIdTest() {
        Wallet wallet = new Wallet();
        wallet.setUserId(1);
        Card card = new Card();
        List<Card> cardList = new ArrayList<>();
        cardList.add(card);
        wallet.setCardList(cardList);
        Page<Card> cards = new PageImpl(cardList);

        Mockito.when(cardRepository.findAllByWallet(any(), any())).thenReturn(cards);

        cardService.getAllByUserId(wallet.getUserId(), Pageable.unpaged());
        verify(cardRepository,times(1)).findAllByWallet(any(), any());

    }

    @Test
    public void addCardTest(){
        User user = new User();
        Wallet wallet = new Wallet();
        Card card = new Card();
        card.setCardHolderName("Petyo");
        card.setNumber("1234567890123456");
        card.setCsv("123");
        card.setExpireDate("12/22");
        card.setWallet(wallet);

        when(userRepository.getById(user.getId())).thenReturn(user);
        when(walletRepository.getByUserId(user.getId())).thenReturn(wallet);
        ArgumentCaptor<Card> cardCaptor = ArgumentCaptor.forClass(Card.class);

        cardService.addCard(user.getId(),card);

        verify(walletRepository,times(1)).save(wallet);
        verify(cardRepository,times(1)).save(cardCaptor.capture());

//        cardCaptor.getValue().getCardHolderName();
        Assert.assertEquals(card.getCardHolderName(),cardCaptor.getValue().getCardHolderName());
    }

    @Test(expected = InvalidCardInputException.class)
    public void shouldThrowException_whenNumberIsInvalid() {
        User user = new User();
        Wallet wallet = new Wallet();
        Card card = new Card();
        card.setCardHolderName("Petyo");
        card.setNumber("1324");
        card.setCsv("123");
        card.setExpireDate("12/22");
        card.setWallet(wallet);

        when(userRepository.getById(user.getId())).thenReturn(user);
        when(walletRepository.getByUserId(user.getId())).thenReturn(wallet);

        cardService.addCard(user.getId(), card);
    }

    @Test(expected = InvalidCardInputException.class)
    public void shouldThrowException_whenCsvIsInvalid() {
        User user = new User();
        Wallet wallet = new Wallet();
        Card card = new Card();
        card.setCardHolderName("Petyo");
        card.setNumber("1234567890123456");
        card.setCsv("1234");
        card.setExpireDate("12/22");
        card.setWallet(wallet);

        when(userRepository.getById(user.getId())).thenReturn(user);
        when(walletRepository.getByUserId(user.getId())).thenReturn(wallet);

        cardService.addCard(user.getId(), card);
    }

    @Test(expected = InvalidCardInputException.class)
    public void shouldThrowException_whenMonthIsAhead() {
        User user = new User();
        Wallet wallet = new Wallet();
        Card card = new Card();
        card.setCardHolderName("Petyo");
        card.setNumber("1234567890123456");
        card.setCsv("123");
        card.setExpireDate("13/22");
        card.setWallet(wallet);

        when(userRepository.getById(user.getId())).thenReturn(user);
        when(walletRepository.getByUserId(user.getId())).thenReturn(wallet);

        cardService.addCard(user.getId(), card);
    }

    @Test(expected = InvalidCardInputException.class)
    public void shouldThrowException_whenMonthIsBehind() {
        User user = new User();
        Wallet wallet = new Wallet();
        Card card = new Card();
        card.setCardHolderName("Petyo");
        card.setNumber("1234567890123456");
        card.setCsv("123");
        card.setExpireDate("-1/22");
        card.setWallet(wallet);

        when(userRepository.getById(user.getId())).thenReturn(user);
        when(walletRepository.getByUserId(user.getId())).thenReturn(wallet);

        cardService.addCard(user.getId(), card);
    }

    @Test(expected = InvalidCardInputException.class)
    public void shouldThrowException_whenYearIsIncorrect() {
        User user = new User();
        Wallet wallet = new Wallet();
        Card card = new Card();
        card.setCardHolderName("Petyo");
        card.setNumber("1234567890123456");
        card.setCsv("123");
        card.setExpireDate("12/18");
        card.setWallet(wallet);

        when(userRepository.getById(user.getId())).thenReturn(user);
        when(walletRepository.getByUserId(user.getId())).thenReturn(wallet);

        cardService.addCard(user.getId(), card);
    }

    @Test(expected = InvalidCardInputException.class)
    public void shouldThrowException_whenStringHasNoSlash() {
        User user = new User();
        Wallet wallet = new Wallet();
        Card card = new Card();
        card.setCardHolderName("Petyo");
        card.setNumber("1234567890123456");
        card.setCsv("123");
        card.setExpireDate("12222");
        card.setWallet(wallet);

        when(userRepository.getById(user.getId())).thenReturn(user);
        when(walletRepository.getByUserId(user.getId())).thenReturn(wallet);

        cardService.addCard(user.getId(), card);
    }
}
