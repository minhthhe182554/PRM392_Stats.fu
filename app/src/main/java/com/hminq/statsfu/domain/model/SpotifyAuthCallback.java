package com.hminq.statsfu.domain.model;

public class SpotifyAuthCallback {
    private final String code;
    private final String state;
    private final String error;
    private final String errorDescription;

    public SpotifyAuthCallback(String code, String state, String error, String errorDescription) {
        this.code = code;
        this.state = state;
        this.error = error;
        this.errorDescription = errorDescription;
    }

    public String getCode() { return code; }
    public String getState() { return state; }
    public String getError() { return error; }
    public String getErrorDescription() { return errorDescription; }

    public boolean hasError() { return error != null; }
    public boolean isValid() { return code != null && !hasError(); }

    @Override
    public String toString() {
        return "SpotifyAuthCallback{" +
                "code='" + code + '\'' +
                ", state='" + state + '\'' +
                ", error='" + error + '\'' +
                ", errorDescription='" + errorDescription + '\'' +
                '}';
    }
}
