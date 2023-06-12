package com.example.virtualwallet.services.impl;

import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.exceptions.InvalidUserInputException;
import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.models.UserVerification;
import com.example.virtualwallet.models.Wallet;
import com.example.virtualwallet.repositories.UserRepository;
import com.example.virtualwallet.repositories.UserVerificationRepository;
import com.example.virtualwallet.repositories.WalletRepository;
import com.example.virtualwallet.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

//    @Autowired
    private UserRepository userRepository ;
//    @Autowired
    private WalletRepository walletRepository;
//    @Autowired
    private UserDetailsManager userDetailsManager;
    private PasswordEncoder passwordEncoder;
//    @Autowired
    private UserVerificationRepository userVerificationRepository;
    private EmailSenderService emailSenderService;

   private final JavaMailSender mailSender;


//    public UserServiceImpl(){
//
//    }
//


    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           WalletRepository walletRepository,
                           UserDetailsManager userDetailsManager,
                           PasswordEncoder passwordEncoder,
                           UserVerificationRepository userVerificationRepository,
                           EmailSenderService emailSenderService,
                           JavaMailSender mailSender
                           ) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.userVerificationRepository = userVerificationRepository;
        this.emailSenderService = emailSenderService;
        this.mailSender = mailSender;
    }

    @Override
    public void create(int id,User user, MultipartFile multipartFile) throws MessagingException, UnsupportedEncodingException {
        if(userRepository.existsByUsername(user.getUsername())){
            throw new DuplicateEntityException(String.format(
                    "User with username %s already exists!",user.getUsername()));
        }

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_ADMIN");
        org.springframework.security.core.userdetails.User userDetails =
                new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        passwordEncoder.encode(user.getPassword()),
                        authorities);

        userDetailsManager.createUser(userDetails);
        User newUser = userRepository.getByUsername(user.getUsername()).get(0);

        String path = uploadFile(multipartFile);
        user.setProfilePicture(path);

        Wallet wallet = new Wallet();
        wallet.setUserId(newUser.getId());
        walletRepository.save(wallet);
        newUser.setWallet(wallet);

        update(newUser.getId(),user,multipartFile, null);

        sendRegistrationCode(newUser);
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> getAllUnidentified(Pageable pageable) {
        return userRepository.findAllByIdentifiedIs(false, pageable);
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public void update(int id, User user, MultipartFile multipartFile, MultipartFile multipartFileId) {
        User newUser = userRepository.getById(id);
        if(newUser.getFirstName() != null) {
            if(!multipartFileId.isEmpty()){
                String nameImage = uploadFile(multipartFileId);
                newUser.setIdentityPicture(nameImage);
                newUser.setIdentified(false);
            }
        }
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setEmail(user.getEmail());
        if(!multipartFile.isEmpty()){
            String nameImage = uploadFile(multipartFile);
            newUser.setProfilePicture(nameImage);
        }
        validateUserInput(newUser);
        userRepository.save(newUser);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public void block(User user) {
        if(user.isBlocked()){
            user.setBlocked(false);
        } else {
            user.setBlocked(true);
        }
        userRepository.save(user);
    }

    @Override
    public void identify(User user, boolean approved) {
        if(approved) {
            user.setIdentified(true);
            user.setBlocked(false);
        } else {
            user.setIdentified(false);
            user.setBlocked(true);
        }
        userRepository.save(user);
    }

    @Override
    public List<Transaction> getTransaction(User user) {
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.addAll(user.getSenderTransactions());
        transactionList.addAll(user.getReceiverTransactions());
        return transactionList;
    }

    @Override
    public List<User> getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public  List<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return getByUsername(username);
    }

    @Override
    public Page<User> findUser(String search, Pageable pageable) {
        return userRepository.findAllByEmailLikeOrPhoneNumberLikeOrUsernameLike(search, search, search, pageable);
    }

    @Override
    public Page<User> findRecipient(String search, Pageable pageable) {
        return userRepository.findAllByFirstNameLikeOrLastNameLike(search, search, pageable);
    }

    @Override
    public void confirmCode(String code) {

        UserVerification token = userVerificationRepository.findByCode(code);

        if(token.getCode().equals(code)){
            token.setVerified(true);
            userVerificationRepository.save(token);
        }
        else {
            throw new IllegalArgumentException("Wrong code");
        }
    }

    @Override
    public void sendRegistrationCode(User user) throws MessagingException, UnsupportedEncodingException {
        UserVerification token = new UserVerification(user);
        token.setVerified(false);
        userVerificationRepository.save(token);
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(user.getEmail());
//        message.setSubject("Complete Registration!");
//        message.setFrom("vakovpetyo@gmail.com");
//        message.setText(token.getCode());
//        emailSenderService.sendEmail(message);
        String toAddress = user.getEmail();
        String fromAddress = "team12vteacher@zohomail.eu";
        String senderName = "Team 12";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Team 12.";

        MimeMessage message = mailSender.createMimeMessage();
        message.setText(token.getCode());
        MimeMessageHelper helper = new MimeMessageHelper(message);
//        final String siteURL = "http://localhost:8888";
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

//        content = content.replace("[[name]]", user.getFirstName());
//        String verifyURL = siteURL + "/confirm-account" + user.getUserVerifications();
//
//        content = content.replace("[[URL]]", verifyURL);
//
//        helper.setText(content, true);

        emailSenderService.sendEmail(message);

    }

    private String uploadFile(MultipartFile file) {
        if(!file.isEmpty()){
            try {
                byte[] bytes = file.getBytes();
                String rootPath = "C:\\uploads";
                File dir = new File("C:/");
                if(!dir.exists()){
                    dir.mkdirs();
                }
                String name = String.valueOf("/uploads/"+new Date().getTime()) + ".jpg";
                File serverFile = new File(dir.getAbsolutePath()
                        +File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(
                        Files.newOutputStream(serverFile.toPath()));
                stream.write(bytes);

                return name;
            }
            catch (IOException e){
                e.printStackTrace();
            }
        } else {
            return "/uploads/user.jpg";
        }
        return null;
    }

    private void validateUserInput(User user) {
        if (user.getUsername() == null || user.getUsername().length() < 4 || user.getUsername().length() > 25) {
            throw new InvalidUserInputException("Invalid user username");
        }
        if (user.getFirstName() == null || user.getFirstName().length() < 2 || user.getFirstName().length() > 15) {
            throw new InvalidUserInputException("Invalid user first name");
        }
        if (user.getLastName() == null || user.getLastName().length() < 4 || user.getLastName().length() > 20) {
            throw new InvalidUserInputException("Invalid user last name");
        }
    }
}
