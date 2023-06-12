package com.example.virtualwallet.services.impl;

import com.example.virtualwallet.models.UserVerification;
import com.example.virtualwallet.repositories.UserVerificationRepository;
import com.example.virtualwallet.services.UserVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserVerificationServiceImpl implements UserVerificationService {
    private UserVerificationRepository userVerificationRepository;

    @Autowired
    public UserVerificationServiceImpl(UserVerificationRepository userVerificationRepository) {
        this.userVerificationRepository = userVerificationRepository;
    }

    @Override
    public void create(UserVerification userVerification) {
        userVerificationRepository.save(userVerification);
    }

    @Override
    public void update(UserVerification userVerification) {
        userVerificationRepository.save(userVerification);
    }

}
