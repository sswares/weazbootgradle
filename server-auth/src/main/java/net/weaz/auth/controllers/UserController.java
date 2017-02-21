package net.weaz.auth.controllers;

import net.weaz.auth.annotations.CurrentUser;
import net.weaz.auth.security.userdetails.CustomUserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user")
    public CustomUserDetails user(@CurrentUser CustomUserDetails currentUser) {
        return currentUser;
    }
}