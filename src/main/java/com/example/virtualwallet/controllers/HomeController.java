package com.example.virtualwallet.controllers;

import com.example.virtualwallet.models.Authorities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomeController {


    @Autowired
    public HomeController() {

    }

    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }


    @PostMapping("/authenticate")
    public String showRegisterConfirmation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Authorities authorities = new Authorities();
        authorities.getAuthority().equals(authentication);
        return "user";
    }

    @GetMapping("/access-denied")
    public String showAccessDenied() {
        return "access-denied";
    }

}
