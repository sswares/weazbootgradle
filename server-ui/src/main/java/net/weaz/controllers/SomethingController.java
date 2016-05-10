package net.weaz.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SomethingController {

    @RequestMapping(value = "/something", method = RequestMethod.GET)
    public String something() {
        return "SUHDUDE";
    }
}
