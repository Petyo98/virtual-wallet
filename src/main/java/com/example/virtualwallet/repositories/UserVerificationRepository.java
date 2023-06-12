package com.example.virtualwallet.repositories;

import com.example.virtualwallet.models.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserVerificationRepository extends JpaRepository<UserVerification, Integer> {

      UserVerification findByCode(String code);
}
