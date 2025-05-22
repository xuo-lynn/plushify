package com.xuo.wrappedmini.controllers;
import com.xuo.wrappedmini.services.SpotifyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

@RestController
public class HomeController {

    private final SpotifyService spotifyService;

    public HomeController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }
    
    @GetMapping("/")
    public String home(OAuth2AuthenticationToken authentication) {
        String welcome = "Welcome to Wrapped Mini!<br/><br/>";

        if (authentication == null) {
            return welcome + "Please log in via Spotify first!";
        }

        String spotifyData = spotifyService.getTopTracks(authentication);
        return welcome + spotifyData;
    }

}
