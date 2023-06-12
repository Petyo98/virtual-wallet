package com.example.virtualwallet.services;

import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.exceptions.InvalidUserInputException;
import com.example.virtualwallet.models.*;
import com.example.virtualwallet.repositories.CardRepository;
import com.example.virtualwallet.repositories.UserRepository;
import com.example.virtualwallet.repositories.UserVerificationRepository;
import com.example.virtualwallet.repositories.WalletRepository;
import com.example.virtualwallet.services.impl.EmailSenderService;
import com.example.virtualwallet.services.impl.UserServiceImpl;
import com.example.virtualwallet.services.impl.WalletServiceImpl;
import org.apache.naming.factory.SendMailFactory;
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
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTests {

    @Mock
    UserRepository userRepository;

    @Mock
    UserDetailsManager userDetailsManager;

    @Mock
    UserVerificationRepository userVerificationRepository;

    @Mock
    WalletRepository walletRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    CardRepository cardRepository;

    @InjectMocks
    UserServiceImpl userService;

    @InjectMocks
    WalletServiceImpl walletService;

    @Mock
    EmailSenderService emailSenderService;

    @Test
    public void getByIdShould_ReturnTag_WhenUserExists(){
        User user = new User();
        user.setId(1);

        when(userRepository.getById(1))
                .thenReturn(user);

        User result = userService.getById(1);

        Assert.assertEquals(result.getId(),user.getId());
    }


    @Test
    public void confirmCodeTest(){
        UserVerification token = new UserVerification();
        token.setCode("codecodecode");
        when(userVerificationRepository.findByCode(token.getCode())).thenReturn(token);
        userService.confirmCode(token.getCode());
        verify(userVerificationRepository,times(1)).save(token);
    }

    @Test
    public void getTransactionTest(){
        List<Transaction> receiver = new ArrayList<>();
        List<Transaction> sender = new ArrayList<>();
        User user = new User();
        Transaction transaction = new Transaction();
        List<Transaction> transactionList = new ArrayList<>();;
        receiver.add(transaction);
        sender.add(transaction);
        user.setSenderTransactions(sender);
        user.setReceiverTransactions(receiver);
        transactionList.addAll(user.getSenderTransactions());
        transactionList.addAll(user.getReceiverTransactions());
        userService.getTransaction(user);
        Assert.assertEquals(2,transactionList.size());
    }

//    @Test
//    public void addCardTest(){
//        User user = new User();
//        Wallet wallet = new Wallet();
//        Card card = new Card();
//        when(userRepository.getById(user.getId())).thenReturn(user);
//        when(walletRepository.getByUserId(user.getId())).thenReturn(wallet);
//        ArgumentCaptor<Card> cardCaptor = ArgumentCaptor.forClass(Card.class);
//
//        userService.addCard(user.getId(),card);
//
//        verify(walletRepository,times(1)).save(wallet);
//        verify(cardRepository,times(1)).save(cardCaptor.capture());
//
////        cardCaptor.getValue().getCardHolderName();
//        // napravi assert sravni imeto na cartata dali e sushtoto kato kartata v wallet-ta
//        Assert.assertEquals(card.getCardHolderName(),cardCaptor.getValue().getCardHolderName());
//    }
//    @Test
//    public void addCardTest1() {
//        //when
//        User user = new User();
//        Card card = new Card();
//        Wallet wallet = new Wallet();
//        when(userRepository.getById(user.getId())).thenReturn(user);
//        when(walletRepository.getByUserId(user.getId())).thenReturn(wallet);
//        //then
//        userService.addCard(user.getId(), card);
//
//        user.getWallet().getCardList().contains(card);
//        //expected
//        //verify
//        //assert
//        //throws
//    }

    @Test
    public void deleteUserTest(){
        User user = new User();

        userService.delete(user);

        verify(userRepository,times(1)).delete(user);

    }

    @Test
    public void updateUserTest(){
        User user = new User();
        user.setUsername("tessssst");
        user.setFirstName("Test");
        user.setLastName("Testov");
        user.setPhoneNumber("08776922100");
        user.setEmail("test@abv.bg");
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());

        when(userRepository.getById(user.getId())).thenReturn(user);

        userService.update(user.getId(),user,file, file);

        verify(userRepository,times(1)).save(user);

    }

    @Test(expected = InvalidUserInputException.class)
    public void updateUserShouldThrowException_whenUserNameTooLong(){
        User user = new User();
        user.setUsername("tes");
        user.setFirstName("Test");
        user.setLastName("Testov");
        user.setPhoneNumber("08776922100");
        user.setEmail("test@abv.bg");
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());

        when(userRepository.getById(user.getId())).thenReturn(user);

        userService.update(user.getId(),user,file, file);

        verify(userRepository,times(1)).save(user);

    }

    @Test(expected = InvalidUserInputException.class)
    public void updateUserShouldThrowException_whenUserNameTooShort(){
        User user = new User();
        user.setUsername("testtesttesttesttesttesttest");
        user.setFirstName("Test");
        user.setLastName("Testov");
        user.setPhoneNumber("08776922100");
        user.setEmail("test@abv.bg");
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());

        when(userRepository.getById(user.getId())).thenReturn(user);

        userService.update(user.getId(),user,file, file);

        verify(userRepository,times(1)).save(user);

    }

    @Test(expected = InvalidUserInputException.class)
    public void updateUserShouldThrowException_whenFirstNameTooLong(){
        User user = new User();
        user.setUsername("tessssst");
        user.setFirstName("T");
        user.setLastName("Testov");
        user.setPhoneNumber("08776922100");
        user.setEmail("test@abv.bg");
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());

        when(userRepository.getById(user.getId())).thenReturn(user);

        userService.update(user.getId(),user,file, file);

        verify(userRepository,times(1)).save(user);

    }

    @Test(expected = InvalidUserInputException.class)
    public void updateUserShouldThrowException_whenFirstNameTooShort(){
        User user = new User();
        user.setUsername("tessssst");
        user.setFirstName("TestTestTestTest");
        user.setLastName("Testov");
        user.setPhoneNumber("08776922100");
        user.setEmail("test@abv.bg");
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());

        when(userRepository.getById(user.getId())).thenReturn(user);

        userService.update(user.getId(),user,file, file);

        verify(userRepository,times(1)).save(user);

    }

    @Test(expected = InvalidUserInputException.class)
    public void updateUserShouldThrowException_whenLastNameTooLong(){
        User user = new User();
        user.setUsername("tessssst");
        user.setFirstName("TestTestTestTest");
        user.setLastName("Tes");
        user.setPhoneNumber("08776922100");
        user.setEmail("test@abv.bg");
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());

        when(userRepository.getById(user.getId())).thenReturn(user);

        userService.update(user.getId(),user,file, file);

        verify(userRepository,times(1)).save(user);

    }

    @Test(expected = InvalidUserInputException.class)
    public void updateUserShouldThrowException_whenLastNameTooShort(){
        User user = new User();
        user.setUsername("tessssst");
        user.setFirstName("Test");
        user.setLastName("TestovTestovTestovTestov");
        user.setPhoneNumber("08776922100");
        user.setEmail("test@abv.bg");
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());

        when(userRepository.getById(user.getId())).thenReturn(user);

        userService.update(user.getId(),user,file, file);

        verify(userRepository,times(1)).save(user);

    }

    @Test
    public void blockUserTest(){
        User user = new User();
        user.setBlocked(false);
        userService.block(user);
        verify(userRepository,times(1)).save(user);
    }

    @Test
    public void unblockUserTest(){
        User user = new User();
        user.setBlocked(true);
        userService.block(user);
        verify(userRepository,times(1)).save(user);
    }

    @Test
    public void identifyUserWhenIdentifiedTest(){
        User user = new User();
        userService.identify(user, true);
        verify(userRepository,times(1)).save(user);
    }

    @Test
    public void identifyUserWhenUnidentifiedTest(){
        User user = new User();
        userService.identify(user, false);
        verify(userRepository,times(1)).save(user);
    }

    @Test
    public void getUserByUsernameTest(){
        User user = new User();
        user.setUsername("testName");
        List<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.getByUsername("testName")).thenReturn(userList);

        User result = userService.getByUsername(user.getUsername()).get(0);

        Assert.assertEquals(result.getUsername(),user.getUsername());

    }

    @Test(expected = DuplicateEntityException.class)
    public void shouldThrowException_whenUserIsDuplicated(){
        User user = new User();
        MockMultipartFile file = new MockMultipartFile("file", "orig", null, "bar".getBytes());

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);
        userService.create(user.getId(),user,file);
        verify(userRepository, times(1)).save(user);
    }


    @Test
    public void sendRegistrationCodeTest() throws MessagingException, UnsupportedEncodingException {
        User user = new User();
        user.setEmail("test@abv.bg");
        Transaction transaction = new Transaction();

        userService.sendRegistrationCode(user);

        verify(emailSenderService,times(1)).sendEmail(any());
    }

    @Test
    public void getAllUsersTest() {
        User user = new User();
        List<User> userList = new ArrayList<>();
        userList.add(user);
        Page<User> users = new PageImpl(userList);
        Mockito.when(userRepository.findAll(Mockito.any(Pageable.class))).thenReturn(users);
        userService.getAll(Pageable.unpaged());
//        List<Card> userPersonalDataDtoList = cardService.getAll(Pageable.unpaged());
        Assert.assertEquals(1, userList.size());
        verify(userRepository,times(1)).findAll(Pageable.unpaged());
    }


    @Test
    public void findRecipientTest() {
        User user = new User();
        user.setFirstName("text");
        user.setLastName("text");
        List<User> transactionList = new ArrayList<>();
        transactionList.add(user);
        Page<User> transactions = new PageImpl(transactionList);

        Mockito.when(userRepository.findAllByFirstNameLikeOrLastNameLike("text","text", Pageable.unpaged()))
                .thenReturn(transactions);
        userService.findRecipient("text",Pageable.unpaged());
        Assert.assertEquals(1, transactionList.size());
        verify(userRepository,times(1))
                .findAllByFirstNameLikeOrLastNameLike("text","text",Pageable.unpaged());

    }

    @Test
    public void findUser() {
        User user = new User();
        user.setFirstName("text");
        user.setLastName("text");
        List<User> transactionList = new ArrayList<>();
        transactionList.add(user);
        Page<User> transactions = new PageImpl(transactionList);

        Mockito.when(userRepository.findAllByEmailLikeOrPhoneNumberLikeOrUsernameLike("text","text","text", Pageable.unpaged()))
                .thenReturn(transactions);
        userService.findUser("text",Pageable.unpaged());
        Assert.assertEquals(1, transactionList.size());
        verify(userRepository,times(1))
                .findAllByEmailLikeOrPhoneNumberLikeOrUsernameLike("text","text","text",Pageable.unpaged());

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowException_whenMoneyIsNegative() {
        Card card = new Card();
        User user = new User();
        double amount = -1;
        String description = "Hello";

        walletService.sendRequestApi(card,user,amount,description);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowException_whenMoneyIsNotPositive() {
        User sender = new User();
        Wallet wallet = new Wallet();
        wallet.setAmount(10);
        sender.setWallet(wallet);
        User receiver = new User();
        double amount = -1;

        walletService.sendMoney(sender, receiver, amount);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowException_whenMoneyIsNotEnough() {
        User sender = new User();
        Wallet wallet = new Wallet();
        wallet.setAmount(10);
        sender.setWallet(wallet);
        User receiver = new User();
        double amount = 500;

        walletService.sendMoney(sender, receiver, amount);
    }

}
