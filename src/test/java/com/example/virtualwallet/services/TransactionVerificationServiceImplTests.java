package com.example.virtualwallet.services;

import com.example.virtualwallet.models.TransactionVerification;
import com.example.virtualwallet.repositories.TransactionVerificationRepository;
import com.example.virtualwallet.services.impl.TransactionVerificationServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TransactionVerificationServiceImplTests {

    @Mock
    TransactionVerificationRepository transactionVerificationRepository;

    @InjectMocks
    TransactionVerificationServiceImpl transactionVerificationService;

    @Test
    public void createTransactionVerificationTest(){
        TransactionVerification transaction = new TransactionVerification();

        transactionVerificationService.create(transaction);

        verify(transactionVerificationRepository,times(1)).save(transaction);

    }

    @Test
    public void updateTransactionVerificationTest(){
        TransactionVerification transaction = new TransactionVerification();

        transactionVerificationService.update(transaction);

        verify(transactionVerificationRepository,times(1)).save(transaction);

    }

    @Test
    public void getByIdShould_ReturnTransaction_WhenTransactionExists(){

        TransactionVerification transaction = new TransactionVerification();
        transaction.setId(1);

        when(transactionVerificationRepository.getById(transaction.getId())).thenReturn(transaction);

        transactionVerificationService.getById(transaction.getId());

        verify(transactionVerificationRepository,timeout(1)).getById(transaction.getId());
    }


}
