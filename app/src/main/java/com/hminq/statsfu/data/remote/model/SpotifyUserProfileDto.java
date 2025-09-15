package com.hminq.statsfu.data.remote.model;

import com.google.gson.annotations.SerializedName;
import com.hminq.statsfu.domain.model.SpotifyUserProfile;

public class SpotifyUserProfileDto {
    @SerializedName("id")
    private String id;

    @SerializedName("display_name")
    private String displayName;
    @SerializedName("email")
    private String email;
    @SerializedName("country")
    private String country;

    @SerializedName("followers")
    private FollowersDto followers;

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public String getEmail() { return email; }
    public String getCountry() { return country; }
    public FollowersDto getFollowers() { return followers; }
    public static class FollowersDto {
        @SerializedName("total")
        private int total;

        public int getTotal() { return total; }
    }

    private SpotifyUserProfile mapUserProfile(SpotifyUserProfileDto dto) {
        int followers = dto.getFollowers() != null ? dto.getFollowers().getTotal() : 0;

        return new SpotifyUserProfile(
                dto.getId(),
                dto.getDisplayName(),
                dto.getEmail(),
                dto.getCountry(),
                getFollowers().getTotal()
        );
    }
}
