package com.hminq.statsfu.domain.model;

import com.hminq.statsfu.BuildConfig;

public final class SpotifyConstants {
    private SpotifyConstants() {}

    public static final String REDIRECT_URI = "statsfu://callback";
    public static final String CLIENT_ID = BuildConfig.SPOTIFY_CLIENT_ID;
    public static final String RESPONSE_TYPE = "code";
    public static final String CODE_CHALLENGE_METHOD = "S256";
    public static final String SCOPES = "user-read-private user-read-email user-read-recently-played user-top-read user-read-currently-playing";
    public static final String AUTH_BASE_URL = "https://accounts.spotify.com/";
    public static final String WEB_API_BASE_URL = "https://api.spotify.com/";
}
