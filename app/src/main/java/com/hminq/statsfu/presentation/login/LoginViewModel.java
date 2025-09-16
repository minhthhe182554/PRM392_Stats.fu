package com.hminq.statsfu.presentation.login;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.hminq.statsfu.domain.model.SpotifyAuthTokenResponse;
import com.hminq.statsfu.domain.model.SpotifyUserProfile;
import com.hminq.statsfu.domain.service.BrowserManager;
import com.hminq.statsfu.domain.usecase.GetUserDetailUseCase;
import com.hminq.statsfu.domain.usecase.LoginUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class LoginViewModel extends AndroidViewModel {
    private static final String TAG = "LoginViewModel";

    private final LoginUseCase loginUseCase;
    private final GetUserDetailUseCase getUserDetailUseCase;
    private final BrowserManager browserManager;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public final MutableLiveData<String> authStatus = new MutableLiveData<>("Click button for login");
    public final MutableLiveData<SpotifyAuthTokenResponse> authToken = new MutableLiveData<>();
    public final MutableLiveData<SpotifyUserProfile> userProfile = new MutableLiveData<>();
    public final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    @Inject
    public LoginViewModel(@NonNull Application application,
                          LoginUseCase loginUseCase,
                          GetUserDetailUseCase getUserDetailUseCase,
                          BrowserManager browserManager) {
        super(application);
        this.loginUseCase = loginUseCase;
        this.getUserDetailUseCase = getUserDetailUseCase;
        this.browserManager = browserManager;
    }

    public void loginWithSpotify() {
        Log.d(TAG, "Starting Spotify login flow");
        authStatus.setValue("Creating auth request...");

        disposables.add(
                loginUseCase.createSpotifyAuthRequest()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                authRequest -> {
                                    Log.d(TAG, "Auth request created, opening browser");
                                    authStatus.setValue("Opening browser...");

                                    try {
                                        browserManager.openUrl(getApplication().getApplicationContext(), authRequest.getAuthUrl());
                                        authStatus.setValue("Waiting for login...");
                                    } catch (Exception e) {
                                        Log.e(TAG, "Failed to open browser: " + e.getMessage(), e);
                                        String errorMsg = "Cannot : " + e.getMessage();
                                        errorMessage.setValue(errorMsg);
                                        authStatus.setValue(errorMsg);
                                    }
                                },
                                error -> {
                                    Log.e(TAG, "Failed to create auth request: " + error.getMessage(), error);
                                    String errorMsg = "Cannot create auth: " + error.getMessage();
                                    errorMessage.setValue(errorMsg);
                                    authStatus.setValue(errorMsg);
                                }
                        )
        );
    }

    public void handleSpotifyCallback(Uri callbackUri) {
        Log.d(TAG, "Handling Spotify callback from ViewModel: " + callbackUri.toString());
        authStatus.setValue("Handing Spotify auth callback...");

        disposables.add(
                loginUseCase.handleAuthCallback(callbackUri)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                tokenResponse -> {
                                    Log.d(TAG, "Authentication successful");
                                    authToken.setValue(tokenResponse);
                                    authStatus.setValue("Login success");

                                    // Now get user profile with the access token
                                    getUserProfile(tokenResponse.getAccessToken());
                                },
                                error -> {
                                    Log.e(TAG, "Authentication failed: " + error.getMessage(), error);
                                    String errorMsg = "Authentication failed: " + error.getMessage();
                                    errorMessage.setValue(errorMsg);
                                    authStatus.setValue(errorMsg);
                                }
                        )
        );
    }

    private void getUserProfile(String accessToken) {
        Log.d(TAG, "Getting user profile with access token");

        disposables.add(
                getUserDetailUseCase.getUserProfile(accessToken)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                profile -> {
                                    Log.d(TAG, "User profile retrieved successfully: " + profile.getDisplayName());
                                    userProfile.setValue(profile);
                                    authStatus.setValue("Login:\n\n" +
                                            "User: " + profile.getDisplayName() + "\n" +
                                            "Email: " + profile.getEmail() + "\n" +
                                            "Country: " + profile.getCountry() + "\n" +
                                            "Followers: " + profile.getFollowers());
                                },
                                error -> {
                                    Log.e(TAG, "Failed to get user profile: " + error.getMessage(), error);
                                    String errorMsg = "Failed to get user profile: " + error.getMessage();
                                    errorMessage.setValue(errorMsg);
                                    authStatus.setValue("Login ok but cannot get user detail");
                                }
                        )
        );
    }

    public void loginWithAppleMusic() {
        // Không implement
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}

