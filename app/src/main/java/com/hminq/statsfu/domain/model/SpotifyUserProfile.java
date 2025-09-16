package com.hminq.statsfu.domain.model;

public class SpotifyUserProfile {
    private String id;
    private String displayName;
    private String email;
    private String country;
    private int followers;

    public SpotifyUserProfile(String id, String displayName, String email, String country, int followers) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.country = country;
        this.followers = followers;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public String getEmail() { return email; }
    public String getCountry() { return country; }
    public int getFollowers() { return followers; }

    @Override
    public String toString() {
        return "SpotifyUserProfile{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", followers=" + followers +
                '}';
    }
}
