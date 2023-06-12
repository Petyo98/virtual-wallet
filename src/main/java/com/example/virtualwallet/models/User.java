package com.example.virtualwallet.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @PositiveOrZero(message = "Id should be positive or zero")
    private int id;

    @Column(name = "username")
    @NotNull
    @NotBlank
    @Size(min = 4, max = 25, message = "Username must be between 5 and 20")
    private String username;

    @Column(name = "password")
    @NotNull
    @Size(min = 5, max = 68, message = "Password must be between 5 and 68")
    private String password;

    @NotNull
    @Size(min = 7, max = 50, message = "Email should be between 7 and 50")
    @Column(name = "email")
    private String email;

    @Size(min = 10, max = 13, message = "Phone number should be between 10 and 13")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Size(min = 2, max = 15, message = "First name should be between 2 and 15")
    @Column(name = "first_name")
    private String firstName;

    @Size(min = 4, max = 20, message = "Last name should be between 4 and 20")
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "identity_picture")
    private String identityPicture;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "identified")
    private boolean identified;

    @Column(name = "blocked")
    private boolean blocked;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private Wallet wallet;

    @JsonIgnore
    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToOne(mappedBy = "userToVerify")
    private UserVerification userVerifications;

    @JsonIgnore
    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(mappedBy = "verifier")
    private List<TransactionVerification> transactionVerifications;

    @JsonIgnore
    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(mappedBy = "sender")
    private List<Transaction> senderTransactions;

    @JsonIgnore
    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(mappedBy = "receiver")
    private List<Transaction> receiverTransactions;

    public User() {
        this.transactionVerifications = new ArrayList<>();
        this.senderTransactions = new ArrayList<>();
        this.receiverTransactions = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getIdentityPicture() {
        return identityPicture;
    }

    public void setIdentityPicture(String identityPicture) {
        this.identityPicture = identityPicture;
    }

    @JsonIgnore
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isIdentified() {
        return identified;
    }

    public void setIdentified(boolean identified) {
        this.identified = identified;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public UserVerification getUserVerifications() {
        return userVerifications;
    }

    public void setUserVerifications(UserVerification userVerifications) {
        this.userVerifications = userVerifications;
    }

    public List<TransactionVerification> getTransactionVerifications() {
        return transactionVerifications;
    }

    public void setTransactionVerifications(List<TransactionVerification> transactionVerifications) {
        this.transactionVerifications = transactionVerifications;
    }

    public List<Transaction> getSenderTransactions() {
        return senderTransactions;
    }

    public void setSenderTransactions(List<Transaction> senderTransactions) {
        this.senderTransactions = senderTransactions;
    }

    public List<Transaction> getReceiverTransactions() {
        return receiverTransactions;
    }

    public void setReceiverTransactions(List<Transaction> receiverTransactions) {
        this.receiverTransactions = receiverTransactions;
    }

}
