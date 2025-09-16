package com.hminq.statsfu.domain.model;

public class SpotifyAuthRequest {
    private final String authUrl;
    private final String codeVerifier;
    private final String state;

    public SpotifyAuthRequest(String authUrl, String codeVerifier, String state) {
        this.authUrl = authUrl;
        this.codeVerifier = codeVerifier;
        this.state = state;
    }

    public String getAuthUrl() { return authUrl; }
    public String getCodeVerifier() { return codeVerifier; }
    public String getState() { return state; }

    @Override
    public String toString() {
        return "SpotifyAuthRequest{" +
                "authUrl='" + authUrl + '\'' +
                ", codeVerifier='" + codeVerifier + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
