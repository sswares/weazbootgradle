package net.weaz.auth.controllers;

import net.weaz.auth.annotations.CurrentUser;
import net.weaz.auth.data.models.AuthCustomUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user")
    //   TODO: make this less ambiguous.  it is actually returning the real type of the principal,
    // which works for our use-case and shows off the current user annotation, but is wrong.  perhaps a spring bug
    public ResponseEntity<AuthCustomUser> user(@CurrentUser AuthCustomUser currentUser) {
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }
}