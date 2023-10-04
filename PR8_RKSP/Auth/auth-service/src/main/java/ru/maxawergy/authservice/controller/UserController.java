package ru.maxawergy.authservice.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.maxawergy.authservice.model.User;

@RestController
public class UserController {


    @GetMapping("/info")
    public String getUserInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return "Hello, " + user.getName() + "\nThis is your auth token (dont forget it): " + user.getToken();
    }
}

