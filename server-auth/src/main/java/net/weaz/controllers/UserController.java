package net.weaz.controllers;

import net.weaz.annotations.CurrentUser;
import net.weaz.models.AuthCustomUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user")
    public ResponseEntity<AuthCustomUser> user(@CurrentUser AuthCustomUser currentUser) {
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }
}