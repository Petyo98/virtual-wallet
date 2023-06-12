package com.example.virtualwallet.services.impl;

import com.example.virtualwallet.models.*;
import com.example.virtualwallet.repositories.TransactionRepository;
import com.example.virtualwallet.repositories.TransactionVerificationRepository;
import com.example.virtualwallet.repositories.UserRepository;
import com.example.virtualwallet.repositories.WalletRepository;
import com.example.virtualwallet.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {
    private WalletRepository walletRepository;
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;
    private EmailSenderService emailSenderService;
    private TransactionVerificationRepository transactionVerificationRepository;

    private final JavaMailSender mailSender;
    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository,
                             UserRepository userRepository,
                             TransactionRepository transactionRepository,
                             EmailSenderService emailSenderService,
                             TransactionVerificationRepository transactionVerificationRepository,
                             JavaMailSender javaMailSender) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.emailSenderService = emailSenderService;
        this.transactionVerificationRepository = transactionVerificationRepository;
        this.mailSender = javaMailSender;
    }

    @Override
    public void update(Wallet wallet) {
        walletRepository.save(wallet);
    }

    @Override
    public List<Wallet> getAll() {
        return walletRepository.findAll();
    }

    @Override
    public Wallet getById(int id) {
        return walletRepository.getById(id);
    }

    @Override
    public Wallet getByUserId(int user_id) {
        return walletRepository.getByUserId(user_id);
    }

    @Override
    @Transactional
    public void addFunds(User user,Card card, double amount) {
        user.getWallet().setAmount(user.getWallet().getAmount() + amount);
        walletRepository.save(user.getWallet());
    }

    @Override
    @Transactional
    public void sendMoney(User sender, User receiver, double amount) throws MessagingException, UnsupportedEncodingException {
        if(sender.getWallet().getAmount() < amount){
            throw new IllegalArgumentException("You don't have enough money!");
        }
        if(amount < 0 ){
            throw new IllegalArgumentException("Money should be bigger than 0!");
        }

        Transaction transaction = new Transaction();
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setDate(LocalDateTime.now());
        transaction.setAmount(amount);
        transaction.setCurrency("BGN");
        transactionRepository.save(transaction);
        if(amount >=2000){
           sendMessage(transaction,sender);
        }
        else {
            sender.getWallet().setAmount(sender.getWallet().getAmount() - amount);
            walletRepository.save(sender.getWallet());
            transaction.setVerified(true);
            receiver.getWallet().setAmount(receiver.getWallet().getAmount() + amount);
            walletRepository.save(receiver.getWallet());
        }
    }

    @Override
    public void sendMessage(Transaction transaction,User sender) throws MessagingException, UnsupportedEncodingException {
        TransactionVerification transactionVerification = new TransactionVerification(transaction);
        transactionVerification.setVerifier(sender);
        transactionVerification.setTransaction(transaction);
        transactionVerification.setVerified(false);
        transactionVerificationRepository.save(transactionVerification);
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(sender.getEmail());
//        message.setSubject("To finish your transaction please verify your code!");
//        message.setFrom("team12vteacher@zohomail.eu");
//        message.setText(transactionVerification.getCode());

        String toAddress = sender.getEmail();
        String fromAddress = "team12vteacher@zohomail.eu";
        String senderName = "Team 12";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Team 12.";

        MimeMessage message = mailSender.createMimeMessage();
        message.setText(transactionVerification.getCode());
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        emailSenderService.sendEmail(message);
    }

    @Override
    @Transactional
    public void confirmCode(User user1, String code){
        User user = userRepository.getById(user1.getId());
        TransactionVerification confirmCode = user.getTransactionVerifications()
                .get(user.getTransactionVerifications().size()-1);
        if(confirmCode.getCode().equals(code)){
            user.getWallet().setAmount(user.getWallet().getAmount() - confirmCode.getTransaction().getAmount());
            walletRepository.save(user.getWallet());
            user.getTransactionVerifications().get(user.getTransactionVerifications().size()-1).setVerified(true);
            User receiver = confirmCode.getTransaction().getReceiver();
            receiver.getWallet().setAmount(receiver.getWallet().getAmount() + confirmCode.getTransaction().getAmount());
            walletRepository.save(receiver.getWallet());
            user.getSenderTransactions().get(user.getSenderTransactions().size()-1).setVerified(true);
        }
        else {
            throw new IllegalArgumentException("Wrong code");
        }
        transactionVerificationRepository.save(user.getTransactionVerifications()
                .get(user.getTransactionVerifications().size()-1));
    }


    @Override
    public HttpStatus sendRequestApi(Card card, User user, double amount, String description){
        final String url = "http://localhost:8081/make/transaction";

        if(amount < 0 ){
            throw new IllegalArgumentException("Money should be bigger than 0!");
        }
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setCurrency("BGN");
        transaction.setReceiver(user);
        transaction.setSender(user);
        transaction.setCard(card);
        transaction.setVerified(true);
        transaction.setDescription(description);
        transaction.setDate(LocalDateTime.now());
        transactionRepository.save(transaction);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-api-key", "1");
        httpHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Transaction> request = new HttpEntity<>(transaction,httpHeaders);
        return  restTemplate.exchange(url,HttpMethod.POST,request,Transaction.class).getStatusCode();
    }
}
