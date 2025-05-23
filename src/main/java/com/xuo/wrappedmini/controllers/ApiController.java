package com.xuo.wrappedmini.controllers;

import com.xuo.wrappedmini.models.UserProfileModel;
import com.xuo.wrappedmini.models.SongModel;
import com.xuo.wrappedmini.services.SpotifyService;
import com.xuo.wrappedmini.services.ProfileService;
import com.xuo.wrappedmini.services.TopSongsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import java.util.List;

@RestController
public class ApiController {

    private final SpotifyService spotifyService;
    private final ProfileService profileService;

    public ApiController(SpotifyService spotifyService, ProfileService profileService) {
        this.spotifyService = spotifyService;
        this.profileService = profileService;
    }

    @GetMapping("/api/userprofile")
    public UserProfileModel getUserProfile(OAuth2AuthenticationToken authentication) {
        if (authentication == null) {
            // Or throw an exception or return null — depends on your frontend handling
            return null;
        }

        // Get raw JSON from Spotify API via SpotifyService
        String profileJson = spotifyService.getUserProfile(authentication);
        String topTracksJson = spotifyService.getTopTracks(authentication);
        String topArtistsJson = spotifyService.getTopArtists(authentication);

        // Parse JSON into Java objects
        UserProfileModel userProfile = profileService.parseUserProfile(profileJson);
        List<SongModel> topSongs = TopSongsService.getSongs(topTracksJson);
        List<String> topArtists = profileService.parseTopArtists(topArtistsJson);

        // Build UserProfileModel with all fields populated
        return new UserProfileModel(
            userProfile.getDisplayName(),
            userProfile.getProfileImage(),
            topSongs,
            topArtists,
            null  // You can add related artists parsing similarly if needed
        );
    }
}
