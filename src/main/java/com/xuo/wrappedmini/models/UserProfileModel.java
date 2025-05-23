package com.xuo.wrappedmini.models;

import java.util.List;

public class UserProfileModel {
    private String displayName;
    private String profileImage;
    private List<SongModel> topSongs;
    private List<String> topArtists;
    private List<String> relatedArtists;

    public UserProfileModel(String displayName, String profileImage, List<SongModel> topSongs, List<String> topArtists, List<String> relatedArtists) {
        this.displayName = displayName;
        this.profileImage = profileImage;
        this.topSongs = topSongs;
        this.topArtists = topArtists;
        this.relatedArtists = relatedArtists;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfileImage() {
        return profileImage;    
    }

    public List<SongModel> getTopSongs() {
        return topSongs;
    }

    public List<String> getTopArtists() {
        return topArtists;
    }
    public List<String> getRelatedArtists() {
        return relatedArtists;
    }
}
