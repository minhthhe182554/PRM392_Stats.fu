package com.hminq.statsfu.domain.model;

public class SpotifyAuthTokenResponse {
    private String accessToken;
    private String tokenType;
    private String scope;
    private int expiresIn;
    private String refreshToken;

    public SpotifyAuthTokenResponse(String accessToken, String tokenType, String scope, int expiresIn, String refreshToken) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.scope = scope;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() { return accessToken; }
    public String getTokenType() { return tokenType; }
    public String getScope() { return scope; }
    public int getExpiresIn() { return expiresIn; }
    public String getRefreshToken() { return refreshToken; }

    @Override
    public String toString() {
        return "SpotifyTokenResponse{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenType='" + tokenType + '\'' +
                ", scope='" + scope + '\'' +
                ", expiresIn=" + expiresIn +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}
