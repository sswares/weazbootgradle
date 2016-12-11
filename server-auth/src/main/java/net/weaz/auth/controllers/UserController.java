package net.weaz.auth.controllers;

import net.weaz.auth.annotations.CurrentUser;
import net.weaz.auth.data.models.AuthCustomUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user")
    public AuthCustomUser user(@CurrentUser AuthCustomUser currentUser) {
        return currentUser;
    }
}