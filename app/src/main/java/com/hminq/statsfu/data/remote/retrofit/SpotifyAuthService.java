package com.hminq.statsfu.data.remote.retrofit;

import com.hminq.statsfu.data.remote.model.SpotifyAuthTokenResponseDto;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SpotifyAuthService {
    @POST("api/token")
    @FormUrlEncoded
    Single<SpotifyAuthTokenResponseDto> exchangeCodeForToken(
            @Field("grant_type") String grantType,
            @Field("code") String code,
            @Field("redirect_uri") String redirectUri,
            @Field("client_id") String clientId,
            @Field("code_verifier") String codeVerifier
    );
}
