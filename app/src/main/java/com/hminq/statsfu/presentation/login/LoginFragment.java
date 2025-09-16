package com.hminq.statsfu.presentation.login;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.hminq.statsfu.R;
import com.hminq.statsfu.databinding.FragmentLoginBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";
    private FragmentLoginBinding binding;
    private NavController navController;
    private LoginViewModel loginViewModel;

    public LoginFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setLoginViewModel(loginViewModel);
        binding.setLifecycleOwner(this);

        // get NavController
        navController = Navigation.findNavController(view);

        // Observe ViewModel
        loginViewModel.authStatus.observe(getViewLifecycleOwner(), status -> {
            Log.d(TAG, "Auth status: " + status);
        });

        loginViewModel.userProfile.observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                Log.d(TAG, "User profile received: " + profile.getDisplayName());
                // Navigate to home fragment after successful login
                navController.navigate(R.id.action_loginFragment_to_homeFragment);
            }
        });

        loginViewModel.errorMessage.observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Log.e(TAG, "Login error: " + error);
                Toast.makeText(getContext(), "Login failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void handleSpotifyCallback(Uri callbackUri) {
        Log.d(TAG, "Handling Spotify callback in LoginFragment: " + callbackUri.toString());
        if (loginViewModel != null) {
            loginViewModel.handleSpotifyCallback(callbackUri);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}