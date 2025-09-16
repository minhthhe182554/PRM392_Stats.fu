package com.hminq.statsfu.data.repository;

import com.hminq.statsfu.data.remote.model.SpotifyAuthTokenResponseDto;
import com.hminq.statsfu.data.remote.retrofit.SpotifyAuthService;
import com.hminq.statsfu.domain.model.SpotifyAuthTokenResponse;
import com.hminq.statsfu.domain.model.SpotifyConstants;
import com.hminq.statsfu.domain.repository.AuthRepository;
import javax.inject.Inject;
import io.reactivex.rxjava3.core.Single;

public class SpotifyAuthRepositoryImpl implements AuthRepository {
    private final SpotifyAuthService authService;
    private final String REDIRECT_URI = SpotifyConstants.REDIRECT_URI;
    private final String CLIENT_ID = SpotifyConstants.CLIENT_ID;
    @Inject
    public SpotifyAuthRepositoryImpl(SpotifyAuthService apiService) {
        this.authService = apiService;
    }

    @Override
    public Single<SpotifyAuthTokenResponse> exchangeCodeForToken(String code, String codeVerifier) {
        return authService.exchangeCodeForToken("authorization_code", code, REDIRECT_URI, CLIENT_ID, codeVerifier)
                .map(this::mapTokenResponse);
    }

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
