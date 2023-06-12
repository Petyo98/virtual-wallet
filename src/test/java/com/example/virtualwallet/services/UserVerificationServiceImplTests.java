package com.example.virtualwallet.services;

import com.example.virtualwallet.models.TransactionVerification;
import com.example.virtualwallet.models.UserVerification;
import com.example.virtualwallet.repositories.TransactionVerificationRepository;
import com.example.virtualwallet.repositories.UserVerificationRepository;
import com.example.virtualwallet.services.impl.TransactionVerificationServiceImpl;
import com.example.virtualwallet.services.impl.UserVerificationServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserVerificationServiceImplTests {

    @Mock
    UserVerificationRepository userVerificationRepository;

    @InjectMocks
    UserVerificationServiceImpl userVerificationService;

    @Test
    public void createUserVerificationTest(){
        UserVerification userVerification = new UserVerification();

        userVerificationService.create(userVerification);

        verify(userVerificationRepository,times(1)).save(userVerification);

    }

    @Test
    public void updateUserVerificationTest(){
        UserVerification userVerification = new UserVerification();

        userVerificationService.update(userVerification);

        verify(userVerificationRepository,times(1)).save(userVerification);

    }


}
