package com.xuo.wrappedmini.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuo.wrappedmini.models.UserProfileModel;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileService {

    // Existing method for parsing user profile
    public UserProfileModel parseUserProfile(String profileJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(profileJson);

            // Extract display name
            String displayName = rootNode.path("display_name").asText("Spotify User");

            // Extract profile image URL
            String profileImage = "";
            JsonNode imagesNode = rootNode.path("images");
            if (imagesNode.isArray() && imagesNode.size() > 0) {
                profileImage = imagesNode.get(0).path("url").asText("");
            }

            // Create and return the UserProfileModel
            return new UserProfileModel(displayName, profileImage, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return new UserProfileModel("Spotify User", "", null, null, null);
        }
    }

    // New method for parsing top artists
    public List<String> parseTopArtists(String topArtistsJson) {
        List<String> topArtists = new ArrayList<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(topArtistsJson);
            JsonNode itemsNode = rootNode.path("items");

            if (itemsNode.isArray()) {
                for (JsonNode artistNode : itemsNode) {
                    String artistName = artistNode.path("name").asText();
                    topArtists.add(artistName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topArtists;
    }
}
