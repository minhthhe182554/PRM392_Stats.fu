package com.hminq.statsfu.domain.usecase;

import android.net.Uri;
import android.util.Log;

import com.hminq.statsfu.domain.model.SpotifyAuthCallback;
import com.hminq.statsfu.domain.model.SpotifyAuthRequest;
import com.hminq.statsfu.domain.model.SpotifyAuthTokenResponse;
import com.hminq.statsfu.domain.repository.AuthRepository;
import com.hminq.statsfu.domain.service.SpotifyAuthManager;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class LoginUseCase {
    private static final String TAG = "LoginUseCase";
    private final AuthRepository authRepository;
    private final SpotifyAuthManager authManager;

    // Store current auth request for validation
    private SpotifyAuthRequest currentAuthRequest;

    @Inject
    public LoginUseCase(AuthRepository authRepository, SpotifyAuthManager authManager) {
        this.authRepository = authRepository;
        this.authManager = authManager;
    }

    public Single<SpotifyAuthRequest> createSpotifyAuthRequest() {
        Log.d(TAG, "Creating Spotify auth request");

        return Single.fromCallable(() -> {
            currentAuthRequest = authManager.createAuthRequest();
            Log.d(TAG, "Auth request created: " + currentAuthRequest.toString());
            return currentAuthRequest;
        });
    }

    public Single<SpotifyAuthTokenResponse> handleAuthCallback(Uri callbackUri) {
        Log.d(TAG, "Handling auth callback: " + callbackUri.toString());

        return Single.fromCallable(() -> {
                    // Validate callback URI
                    if (!authManager.isValidCallback(callbackUri)) {
                        throw new IllegalArgumentException("Invalid callback URI");
                    }

                    // Parse callback
                    SpotifyAuthCallback callback = authManager.parseCallback(callbackUri);

                    // Validate state
                    if (currentAuthRequest == null) {
                        throw new IllegalStateException("No active auth request found");
                    }

                    if (!authManager.isStateValid(callback.getState(), currentAuthRequest.getState())) {
                        throw new SecurityException("Invalid state parameter");
                    }

                    // Check for errors
                    if (callback.hasError()) {
                        String errorMsg = "Auth error: " + callback.getError();
                        if (callback.getErrorDescription() != null) {
                            errorMsg += " - " + callback.getErrorDescription();
                        }
                        throw new RuntimeException(errorMsg);
                    }

                    // Validate we have code
                    if (!callback.isValid()) {
                        throw new RuntimeException("No authorization code received");
                    }

                    return callback;
                })
                .flatMap(callback -> {
                    Log.d(TAG, "Callback validation successful, exchanging code for token");
                    return exchangeCodeForToken(callback.getCode(), currentAuthRequest.getCodeVerifier());
                })
                .doOnSuccess(tokenResponse -> {
                    Log.d(TAG, "Authentication flow completed successfully. Access token obtained.");
                    currentAuthRequest = null; // Clear stored request
                })
                .doOnError(error -> {
                    Log.e(TAG, "Authentication flow failed: " + error.getMessage(), error);
                    currentAuthRequest = null; // Clear stored request on error
                });
    }

    public Single<SpotifyAuthTokenResponse> exchangeCodeForToken(String code, String codeVerifier) {
        Log.d(TAG, "Exchanging code for token...");
        return authRepository.exchangeCodeForToken(code, codeVerifier)
                .doOnSuccess(response -> {
                    Log.d(TAG, "Token exchange successful: " + response.toString());
                })
                .doOnError(error -> {
                    Log.e(TAG, "Token exchange failed: " + error.getMessage(), error);
                });
    }
}
