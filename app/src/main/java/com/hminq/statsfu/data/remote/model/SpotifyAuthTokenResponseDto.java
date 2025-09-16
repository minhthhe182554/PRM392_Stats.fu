package com.hminq.statsfu.data.remote.model;

import com.google.gson.annotations.SerializedName;
import com.hminq.statsfu.domain.model.SpotifyAuthTokenResponse;

public class SpotifyAuthTokenResponseDto {
    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("scope")
    private String scope;

    @SerializedName("expires_in")
    private int expiresIn;

    @SerializedName("refresh_token")
    private String refreshToken;

    // Getters
    public String getAccessToken() { return accessToken; }
    public String getTokenType() { return tokenType; }
    public String getScope() { return scope; }
    public int getExpiresIn() { return expiresIn; }
    public String getRefreshToken() { return refreshToken; }

    private SpotifyAuthTokenResponse mapTokenResponse(SpotifyAuthTokenResponseDto dto) {
        return new SpotifyAuthTokenResponse(
                dto.getAccessToken(),
                dto.getTokenType(),
                dto.getScope(),
                dto.getExpiresIn(),
                dto.getRefreshToken()
        );
    }
}
