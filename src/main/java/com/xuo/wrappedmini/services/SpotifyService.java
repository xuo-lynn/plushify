package com.xuo.wrappedmini.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Service
public class SpotifyService {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final RestTemplate restTemplate;

    @Autowired
    public SpotifyService(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
        this.restTemplate = new RestTemplate();
    }

    public String getTopTracks(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me/top/tracks?limit=10",
                HttpMethod.GET,
                entity,
                String.class
        );
        return response.getBody();
    }

    /**
     * Get the user's Spotify profile information
     * @param authentication The OAuth2 authentication token
     * @return JSON string containing user profile data
     */
    public String getUserProfile(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me",
                HttpMethod.GET,
                entity,
                String.class
        );
        
        return response.getBody();
    }
    
    /**
     * Get the user's top artists from Spotify
     * @param authentication The OAuth2 authentication token
     * @param limit Optional limit parameter (default is 10)
     * @return JSON string containing top artists data
     */
    public String getTopArtists(OAuth2AuthenticationToken authentication, int limit) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.spotify.com/v1/me/top/artists?limit=" + limit,
                HttpMethod.GET,
                entity,
                String.class
        );
        
        return response.getBody();
    }
    
    /**
     * Overloaded method with default limit of 10
     */
    public String getTopArtists(OAuth2AuthenticationToken authentication) {
        return getTopArtists(authentication, 10);
    }
    
    /**
     * Get artists related to a specific artist
     * @param authentication The OAuth2 authentication token
     * @param artistId The Spotify ID of the artist
     * @return JSON string containing related artists data
     */
    public String getRelatedArtists(OAuth2AuthenticationToken authentication, String artistId) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName()
        );

        String accessToken = client.getAccessToken().getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.spotify.com/v1/artists/" + artistId + "/related-artists",
                HttpMethod.GET,
                entity,
                String.class
        );
        
        return response.getBody();
    }
}
