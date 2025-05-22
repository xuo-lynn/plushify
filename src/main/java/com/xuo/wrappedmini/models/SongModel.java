package com.xuo.wrappedmini.models;
import java.util.List;

public class SongModel {
    private String title;
    private List<String> artists;
    private String album;
    private String albumImage;
    private Integer duration; 

    public SongModel(String title, List<String> artists, String album, String albumImage, Integer duration) {
        this.title = title;
        this.artists = artists;
        this.album = album;
        this.albumImage = albumImage;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getArtists() {
        return artists;
    }

    public String getAlbum() {
        return album;
    }

    public String getAlbumImage() {
        return albumImage;
    }

    public Integer getDuration() {
        return duration;
    }
}
