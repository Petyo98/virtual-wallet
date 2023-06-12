package com.example.virtualwallet.services;

import com.example.virtualwallet.repositories.WalletRepository;
import com.example.virtualwallet.services.impl.CardServiceImpl;
import com.example.virtualwallet.services.impl.EmailSenderService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@RunWith(MockitoJUnitRunner.class)
public class EmailSenderServiceTests {

    @Mock
    JavaMailSender javaMailSender;

    @InjectMocks
    EmailSenderService emailSenderService;

//    @Test
//    public void sendMessageTest(){
//        SimpleMailMessage email = new SimpleMailMessage();
//
//        emailSenderService.sendEmail(email);
//
//        Mockito.verify(javaMailSender,Mockito.times(1)).send(email);
//    }
//}
