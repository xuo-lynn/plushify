package com.xuo.wrappedmini.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/loginSpotify")
    public String loginSpotify() {
        return "redirect:/oauth2/authorization/spotify";
    }
}

