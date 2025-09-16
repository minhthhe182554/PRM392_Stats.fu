package com.hminq.statsfu.domain.usecase;

import android.util.Log;

import com.hminq.statsfu.domain.model.SpotifyUserProfile;
import com.hminq.statsfu.domain.repository.UserRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class GetUserDetailUseCase {
    private static final String TAG = "GetUserDetailUseCase";

    private final UserRepository userRepository;

    @Inject
    public GetUserDetailUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Single<SpotifyUserProfile> getUserProfile(String accessToken) {
        Log.d(TAG, "Getting user profile with access token");
        return userRepository.getUserProfile(accessToken)
                .doOnSuccess(profile -> {
                    Log.d(TAG, "User profile retrieved successfully: " + profile.toString());
                })
                .doOnError(error -> {
                    Log.e(TAG, "Failed to get user profile: " + error.getMessage(), error);
                });
    }
}
