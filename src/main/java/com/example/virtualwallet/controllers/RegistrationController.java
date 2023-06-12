package com.example.virtualwallet.controllers;

import com.example.virtualwallet.models.User;
import com.example.virtualwallet.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@Controller
public class RegistrationController {

    private UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/register")
    public String showRegister(){
        return "index";
    }

    @PostMapping(value = "/register")
    public String registerUser(@Valid @ModelAttribute("user")User user,
                               @RequestParam("file") MultipartFile multipart) throws MessagingException, UnsupportedEncodingException {
        userService.create(user.getId(),user, multipart);
        return "registration-confirmation";
    }

    @PostMapping("/confirm-account")
    public String confirmUserAccount(@RequestParam("code")String code)
    {
        userService.confirmCode(code);
        return "index";
    }

    @GetMapping("user/details")
    public String editUser(Model model) {
        User user = userService.getCurrentUser().get(0);
        model.addAttribute("user", user);
        return "user-details";
    }
}
