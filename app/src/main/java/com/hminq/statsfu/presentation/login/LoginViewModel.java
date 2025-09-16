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

    public final MutableLiveData<String> authStatus = new MutableLiveData<>("Nhấn button để login");
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
        Log.d(TAG, "Starting Spotify login flow from ViewModel");
        authStatus.setValue("🔄 Đang tạo auth request...");

        disposables.add(
                loginUseCase.createSpotifyAuthRequest()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                authRequest -> {
                                    Log.d(TAG, "Auth request created, opening browser");
                                    authStatus.setValue("🔄 Đang mở trình duyệt...");

                                    try {
                                        browserManager.openUrl(getApplication().getApplicationContext(), authRequest.getAuthUrl());
                                        authStatus.setValue("🔄 Đang chờ đăng nhập...");
                                    } catch (Exception e) {
                                        Log.e(TAG, "Failed to open browser: " + e.getMessage(), e);
                                        String errorMsg = "❌ Không thể mở trình duyệt: " + e.getMessage();
                                        errorMessage.setValue(errorMsg);
                                        authStatus.setValue(errorMsg);
                                    }
                                },
                                error -> {
                                    Log.e(TAG, "Failed to create auth request: " + error.getMessage(), error);
                                    String errorMsg = "❌ Không thể tạo auth request: " + error.getMessage();
                                    errorMessage.setValue(errorMsg);
                                    authStatus.setValue(errorMsg);
                                }
                        )
        );
    }

    public void handleSpotifyCallback(Uri callbackUri) {
        Log.d(TAG, "Handling Spotify callback from ViewModel: " + callbackUri.toString());
        authStatus.setValue("🔄 Đang xử lý đăng nhập...");

        disposables.add(
                loginUseCase.handleAuthCallback(callbackUri)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                tokenResponse -> {
                                    Log.d(TAG, "Authentication successful, got access token");
                                    authToken.setValue(tokenResponse);
                                    authStatus.setValue("✅ Xác thực thành công! Đang lấy thông tin user...");

                                    // Now get user profile with the access token
                                    getUserProfile(tokenResponse.getAccessToken());
                                },
                                error -> {
                                    Log.e(TAG, "Authentication failed: " + error.getMessage(), error);
                                    String errorMsg = "❌ Đăng nhập thất bại: " + error.getMessage();
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
                                    authStatus.setValue("✅ Đăng nhập thành công!\n\n" +
                                            "User: " + profile.getDisplayName() + "\n" +
                                            "Email: " + profile.getEmail() + "\n" +
                                            "Country: " + profile.getCountry() + "\n" +
                                            "Followers: " + profile.getFollowers());
                                },
                                error -> {
                                    Log.e(TAG, "Failed to get user profile: " + error.getMessage(), error);
                                    String errorMsg = "❌ Không thể lấy thông tin user: " + error.getMessage();
                                    errorMessage.setValue(errorMsg);
                                    authStatus.setValue("✅ Đăng nhập thành công nhưng không lấy được thông tin user");
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

