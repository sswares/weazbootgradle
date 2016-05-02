package net.weaz.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserGreetingController {

    @RequestMapping("/greeting")
    public GreetingPresenter hello(Principal principal) {
        return new GreetingPresenter("Hello " + principal.getName() + "!");
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