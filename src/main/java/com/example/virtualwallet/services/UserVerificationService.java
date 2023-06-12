package com.example.virtualwallet.services;

import com.example.virtualwallet.models.UserVerification;

public interface UserVerificationService {

    void create(UserVerification userVerification);

    void update(UserVerification userVerification);

}
