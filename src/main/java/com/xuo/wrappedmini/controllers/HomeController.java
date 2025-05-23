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
        html.append("<body style='background-color: #f8e1e7;'>");
        html.append("<h2>User Profile</h2>");
        html.append("<div style='text-align: center; font-family: Arial, sans-serif;'>");
        html.append("<img style='border-radius: 50%; width: 150px; height: 150px;' src='").append(userProfile.getProfileImage()).append("' alt='Profile Image'>");
        html.append("<div style='font-size: 24px; font-weight: bold; color: white;'>").append(userProfile.getDisplayName()).append("</div>");
        html.append("<div style='margin-top: 20px;'>");
        html.append("<h2 style='color: white;'>Your Top Tracks</h2>");
        if (!topSongs.isEmpty()) {
            for (SongModel song : topSongs) {
                html.append("<div style='display: flex; align-items: center; background-color: #f8e1e7; color: #333; padding: 10px; border-radius: 8px; margin-bottom: 10px; border: 2px solid #f4c2c2;'>");
                if (song.getAlbumImage() != null && !song.getAlbumImage().isEmpty()) {
                    html.append("<img style='width: 60px; height: 60px; border-radius: 4px; margin-right: 15px;' src='")
                        .append(song.getAlbumImage())
                        .append("' alt='Album Cover'>");
                }
                html.append("<div style='flex-grow: 1;'>");
                html.append("<div style='color: white;'>").append(song.getTitle()).append("</div>");
                if (song.getArtists() != null && !song.getArtists().isEmpty()) {
                    html.append("<div style='color: #b3b3b3;'>").append(String.join(", ", song.getArtists()));
                    if (song.getAlbum() != null && !song.getAlbum().isEmpty()) {
                        html.append(" • ").append(song.getAlbum());
                    }
                    html.append("</div>");
                }
                html.append("<div style='color: #b3b3b3;'>");
                if (song.getDuration() != null) {
                    int minutes = song.getDuration() / 60000;
                    int seconds = (song.getDuration() % 60000) / 1000;
                    html.append(String.format("%02d:%02d", minutes, seconds));
                }
                html.append("</div>");
                html.append("</div>");
                html.append("<div style='position: absolute; right: 10px; top: 10px;'><img src='bow.png' alt='Bow' style='width: 20px; height: 20px;'></div>");
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
        html.append("</div>");
        html.append("</div>");

        return welcome + html.toString();
    }
}
