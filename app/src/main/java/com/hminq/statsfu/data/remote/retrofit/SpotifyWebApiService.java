package com.hminq.statsfu.data.remote.retrofit;

import com.hminq.statsfu.data.remote.model.SpotifyUserProfileDto;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface SpotifyWebApiService {
    @GET("v1/me")
    Single<SpotifyUserProfileDto> getUserProfile(
            @Header("Authorization") String authorization
    );
}
