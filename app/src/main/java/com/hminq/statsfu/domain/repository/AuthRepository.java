package com.hminq.statsfu.domain.repository;

import com.hminq.statsfu.domain.model.SpotifyAuthTokenResponse;

import io.reactivex.rxjava3.core.Single;

public interface AuthRepository {
    Single<SpotifyAuthTokenResponse> exchangeCodeForToken(String code, String codeVerifier);
}
