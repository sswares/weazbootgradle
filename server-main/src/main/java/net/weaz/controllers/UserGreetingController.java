package net.weaz.controllers;

import net.weaz.annotations.CurrentUser;
import net.weaz.models.CustomMainUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserGreetingController {

    @GetMapping("/api/greeting")
    public GreetingPresenter greeting(@CurrentUser CustomMainUser currentUser) {
        return new GreetingPresenter("Hello " + currentUser.getUsername() + "!"
                                     + "  Your favorite cat was " + currentUser.getFavoriteCat() + ", right?");
    }

    class GreetingPresenter {

        private String message;

        GreetingPresenter(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}