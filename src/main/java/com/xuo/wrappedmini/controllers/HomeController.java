package com.xuo.wrappedmini.controllers;
import com.xuo.wrappedmini.services.SpotifyService;
import com.xuo.wrappedmini.services.TopSongsService;
import com.xuo.wrappedmini.services.ProfileService;
import com.xuo.wrappedmini.models.SongModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.xuo.wrappedmini.models.*;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import java.util.List;

@RestController
public class HomeController {

    private final SpotifyService spotifyService;
    private final ProfileService profileService;

    public HomeController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
        this.profileService = new ProfileService();
    }


    @GetMapping("/top-artists")
    public String topArtists(OAuth2AuthenticationToken authentication) {
        if (authentication == null) {
            return "Please log in via Spotify first!";
        }

        // Get top artists JSON from Spotify API
        String topArtistsJson = spotifyService.getTopArtists(authentication);
    
        // Parse the JSON to get a list of top artists
        List<String> topArtists = profileService.parseTopArtists(topArtistsJson);
    
        // Build HTML response with the top artists data
        StringBuilder html = new StringBuilder();
        html.append("<h2>Your Top Artists</h2>");
    
        if (!topArtists.isEmpty()) {
            for (String artist : topArtists) {
                html.append("<div style='margin-bottom: 10px;'>")
                    .append(artist)
                    .append("</div>");
            }
        } else {
            html.append("<p>No top artists found.</p>");
        }
    
        return html.toString();
    }

    @GetMapping("/user-profile")
    public String userProfile(OAuth2AuthenticationToken authentication) {
        if (authentication == null) {
            return "Please log in via Spotify first!";
        }

        // Get user profile JSON from Spotify API
        String userProfileJson = spotifyService.getUserProfile(authentication);

        // Parse the JSON to get UserProfileModel
        UserProfileModel userProfile = profileService.parseUserProfile(userProfileJson);

        // Display the user's display name and profile image
        return "Display Name: " + userProfile.getDisplayName() + "<br/>" +
               "Profile Image: <img src='" + userProfile.getProfileImage() + "' alt='Profile Image' style='width:100px;height:100px;'>";
    }
    
    @GetMapping("/")
    public String home(OAuth2AuthenticationToken authentication) {
        String welcome = "Welcome to Wrapped Mini!<br/><br/>";

        if (authentication == null) {
            return welcome + "Please log in via Spotify first!";
        }

        // Get user profile JSON from Spotify API
        String userProfileJson = spotifyService.getUserProfile(authentication);
        UserProfileModel userProfile = profileService.parseUserProfile(userProfileJson);

        // Get top tracks and top artists JSON from Spotify API
        String topTracksJson = spotifyService.getTopTracks(authentication);
        List<SongModel> topSongs = TopSongsService.getSongs(topTracksJson);

        String topArtistsJson = spotifyService.getTopArtists(authentication);
        List<String> topArtists = profileService.parseTopArtists(topArtistsJson);

        // Build HTML response with user profile, top songs, and top artists
        StringBuilder html = new StringBuilder();
        html.append("<h2>User Profile</h2>");
        html.append("Display Name: ").append(userProfile.getDisplayName()).append("<br/>");
        html.append("Profile Image: <img src='").append(userProfile.getProfileImage()).append("' alt='Profile Image' style='width:100px;height:100px;'><br/><br/>");

        html.append("<h2>Your Top Tracks</h2>");
        if (!topSongs.isEmpty()) {
            for (SongModel song : topSongs) {
                html.append("<div style='margin-bottom: 20px; border-bottom: 1px solid #eee; padding-bottom: 15px;'>");
                if (song.getAlbumImage() != null && !song.getAlbumImage().isEmpty()) {
                    html.append("<img style='float: left; margin-right: 15px; width: 100px; height: 100px;' src='")
                        .append(song.getAlbumImage())
                        .append("' alt='Album Cover'>");
                }
                html.append("<div style='margin-left: 115px;'>");
                html.append("<div style='font-weight: bold; font-size: 18px;'>").append(song.getTitle()).append("</div>");
                if (song.getArtists() != null && !song.getArtists().isEmpty()) {
                    html.append("<div style='color: #666; margin-bottom: 5px;'>by ").append(String.join(", ", song.getArtists())).append("</div>");
                }
                html.append("<div style='font-style: italic; color: #888;'>Album: ").append(song.getAlbum()).append("</div>");
                if (song.getDuration() != null) {
                    int minutes = song.getDuration() / 60000;
                    int seconds = (song.getDuration() % 60000) / 1000;
                    html.append("<div>Duration: ").append(minutes).append(":").append(String.format("%02d", seconds)).append("</div>");
                }
                html.append("</div>");
                html.append("<div style='clear: both;'></div>");
                html.append("</div>");
            }
        } else {
            html.append("<p>No top tracks found.</p>");
        }

        html.append("<h2>Your Top Artists</h2>");
        if (!topArtists.isEmpty()) {
            for (String artist : topArtists) {
                html.append("<div style='margin-bottom: 10px;'>").append(artist).append("</div>");
            }
        } else {
            html.append("<p>No top artists found.</p>");
        }

        return welcome + html.toString();
    }
}
