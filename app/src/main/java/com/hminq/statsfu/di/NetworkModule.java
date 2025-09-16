package com.hminq.statsfu.di;

import com.hminq.statsfu.data.remote.retrofit.SpotifyAuthService;
import com.hminq.statsfu.data.remote.retrofit.SpotifyWebApiService;
import com.hminq.statsfu.data.repository.SpotifyAuthRepositoryImpl;
import com.hminq.statsfu.data.repository.UserRepositoryImpl;
import com.hminq.statsfu.domain.repository.AuthRepository;
import com.hminq.statsfu.domain.repository.UserRepository;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {
    private static final String BASE_AUTH_URL = "https://accounts.spotify.com/";
    private static final String BASE_WEB_API_URL = "https://api.spotify.com/";
    @Qualifier
    public @interface SpotifyAuth {}

    @Qualifier
    public @interface SpotifyWebApi {}

    // Retrofit for Authentication
    @Provides
    @Singleton
    @SpotifyAuth
    public Retrofit provideSpotifyAuthRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_AUTH_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    // Retrofit for Web API
    @Provides
    @Singleton
    @SpotifyWebApi
    public Retrofit provideSpotifyWebApiRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_WEB_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public SpotifyAuthService provideSpotifyAuthService(@SpotifyAuth Retrofit retrofit) {
        return retrofit.create(SpotifyAuthService.class);
    }

    @Provides
    @Singleton
    public SpotifyWebApiService provideSpotifyWebApiService(@SpotifyWebApi Retrofit retrofit) {
        return retrofit.create(SpotifyWebApiService.class);
    }

    @Provides
    @Singleton
    public AuthRepository provideAuthRepository(SpotifyAuthRepositoryImpl repository) {
        return repository;
    }

    @Provides
    @Singleton
    public UserRepository provideUserRepository(UserRepositoryImpl repository) {
        return repository;
    }
}
