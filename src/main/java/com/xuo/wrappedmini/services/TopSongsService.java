package com.xuo.wrappedmini.services;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuo.wrappedmini.models.SongModel;

@Service
public class TopSongsService {
    public static List<SongModel> getSongs(String jsonResponse){
        List<SongModel> songs = new ArrayList<>();
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            JsonNode items = rootNode.path("items");

            if (items.isArray()) {
                for (JsonNode item: items){
                    String title = item.path("name").asText();

                    List<String> artists = new ArrayList<>();
                    JsonNode artistsNode = item.path("artists");

                    if (artistsNode.isArray()) {
                        for (JsonNode artist : artistsNode) {
                            artists.add(artist.path("name").asText());
                        }
                    }
                    
                    String album = item.path("album").path("name").asText();
                    String albumImage = item.path("album").path("images").get(0).path("url").asText();
                    int duration = item.path("duration_ms").asInt();
                    SongModel song = new SongModel(title, artists, album, albumImage, duration);
                    songs.add(song);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return songs;
    }
}
