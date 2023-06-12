package com.example.virtualwallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    @PositiveOrZero(message = "Card ID should be positive or zero")
    private int id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column(name = "number")
    @Size(min = 16, max = 16, message = "Card number should be exactly 16 symbols.")
    private String number;

    @Column(name = "cardholder")
    @Size(min = 5, max = 30, message = "Name on card should be between 5 and 30 symbols.")
    private String cardHolderName;

    @Column(name = "expire_date")
    @Size(min = 5, max = 5, message = "Expiry date should be in the following format: ( MM / YY ).")
    private String expireDate;

    @Column(name = "csv")
    @Size(min = 3, max = 3, message = "Code has to be exactly 3 symbols")
    private String csv;

    @JsonIgnore
    @OneToMany(mappedBy = "card")
    private List<Transaction> transactions;

    @Column(name = "deleted")
    private boolean deleted;

    public Card() {
        this.transactions = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    @JsonIgnore
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

}
