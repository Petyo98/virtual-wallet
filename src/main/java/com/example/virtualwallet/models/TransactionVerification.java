package com.example.virtualwallet.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;


import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "transactions_verification")
public class TransactionVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "verification_id")
    @PositiveOrZero(message = "Verification ID should be positive or zero")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User verifier;

    @OneToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @Column(name = "code")
    @NotNull
    private String code;

    @Column(name = "expires")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date expires;

    @Column(name = "verified")
    private boolean verified;

    public TransactionVerification() {

    }

    public TransactionVerification(Transaction transaction) {
        this.transaction = transaction;
        expires = new Date();
        code = UUID.randomUUID().toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getVerifier() {
        return verifier;
    }

    public void setVerifier(User verifier) {
        this.verifier = verifier;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
