package com.hminq.statsfu.domain.service;

import static android.util.Base64.encodeToString;

import android.net.Uri;
import android.util.Log;

import com.hminq.statsfu.BuildConfig;
import com.hminq.statsfu.domain.model.SpotifyAuthCallback;
import com.hminq.statsfu.domain.model.SpotifyAuthRequest;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SpotifyAuthManager {
    private static final String TAG = "SpotifyAuthManager";
    private static final String REDIRECT_URI = "statsfu://callback";
    private static final String CLIENT_ID = BuildConfig.SPOTIFY_CLIENT_ID;
    private static final String RESPONSE_TYPE = "code";
    private static final String CODE_CHALLENGE_METHOD = "S256";
    private static final String SCOPES = "user-read-private user-read-email user-read-recently-played user-top-read user-read-currently-playing";

    @Inject
    public SpotifyAuthManager() {}

    public SpotifyAuthRequest createAuthRequest() {
        Log.d(TAG, "Creating Spotify auth request");

        String state = UUID.randomUUID().toString();
        String codeVerifier = generateCodeVerifier();
        String codeChallenge = generateCodeChallenge(codeVerifier);

        String authUrl = new Uri.Builder()
                .scheme("https")
                .authority("accounts.spotify.com")
                .path("authorize")
                .appendQueryParameter("response_type", RESPONSE_TYPE)
                .appendQueryParameter("client_id", CLIENT_ID)
                .appendQueryParameter("redirect_uri", REDIRECT_URI)
                .appendQueryParameter("scope", SCOPES)
                .appendQueryParameter("state", state)
                .appendQueryParameter("code_challenge_method", CODE_CHALLENGE_METHOD)
                .appendQueryParameter("code_challenge", codeChallenge)
                .appendQueryParameter("show_dialog", "true")
                .build()
                .toString();

        Log.d(TAG, "Auth URL created: " + authUrl);

        return new SpotifyAuthRequest(authUrl, codeVerifier, state);
    }

    public SpotifyAuthCallback parseCallback(Uri callbackUri) {
        Log.d(TAG, "Parsing callback: " + callbackUri.toString());

        String code = callbackUri.getQueryParameter("code");
        String state = callbackUri.getQueryParameter("state");
        String error = callbackUri.getQueryParameter("error");
        String errorDescription = callbackUri.getQueryParameter("error_description");

        SpotifyAuthCallback callback = new SpotifyAuthCallback(code, state, error, errorDescription);
        Log.d(TAG, "Parsed callback: " + callback.toString());

        return callback;
    }

    public boolean isValidCallback(Uri uri) {
        return uri != null && uri.toString().startsWith(REDIRECT_URI);
    }

    public boolean isStateValid(String callbackState, String expectedState) {
        boolean valid = expectedState != null && expectedState.equals(callbackState);
        Log.d(TAG, "State validation: " + valid + " (expected: " + expectedState + ", got: " + callbackState + ")");
        return valid;
    }

    private String generateCodeVerifier() {
        SecureRandom sr = new SecureRandom();
        byte[] code = new byte[32];
        sr.nextBytes(code);
        return encodeToString(
                code,
                android.util.Base64.URL_SAFE | android.util.Base64.NO_PADDING | android.util.Base64.NO_WRAP
        );
    }

    private String generateCodeChallenge(String verifier) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(verifier.getBytes(StandardCharsets.US_ASCII));
            return encodeToString(hash,
                    android.util.Base64.URL_SAFE | android.util.Base64.NO_PADDING | android.util.Base64.NO_WRAP);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate code challenge", e);
        }
    }

}
