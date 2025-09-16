package com.hminq.statsfu.data.repository;

import com.hminq.statsfu.data.remote.model.SpotifyUserProfileDto;
import com.hminq.statsfu.data.remote.retrofit.SpotifyWebApiService;
import com.hminq.statsfu.domain.model.SpotifyUserProfile;
import com.hminq.statsfu.domain.repository.UserRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class UserRepositoryImpl implements UserRepository {
    private final SpotifyWebApiService webApiService;

    @Inject
    public UserRepositoryImpl(SpotifyWebApiService webApiService) {
        this.webApiService = webApiService;
    }

    @Override
    public Single<SpotifyUserProfile> getUserProfile(String accessToken) {
        return webApiService.getUserProfile("Bearer " + accessToken)
                .map(this::mapUserProfile);
    }

    private SpotifyUserProfile mapUserProfile(SpotifyUserProfileDto dto) {
        int followers = dto.getFollowers() != null ? dto.getFollowers().getTotal() : 0;

        return new SpotifyUserProfile(
                dto.getId(),
                dto.getDisplayName(),
                dto.getEmail(),
                dto.getCountry(),
                followers
        );
    }
}
