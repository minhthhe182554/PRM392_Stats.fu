package com.hminq.statsfu.domain.repository;

import com.hminq.statsfu.domain.model.SpotifyUserProfile;

import io.reactivex.rxjava3.core.Single;

public interface UserRepository {
    Single<SpotifyUserProfile> getUserProfile(String accessToken);
}
