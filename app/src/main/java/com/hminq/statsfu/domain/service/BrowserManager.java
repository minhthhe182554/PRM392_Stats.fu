package com.hminq.statsfu.domain.service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.browser.customtabs.CustomTabsIntent;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
public class BrowserManager {
    private static final String TAG = "BrowserManager";

    @Inject
    public BrowserManager() {}

    public void openUrl(Context context, String url) {
        Log.d(TAG, "Opening URL: " + url);

        Uri uri = Uri.parse(url);

        // Open url with CustomTAbsIntent
        try {
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
            customTabsIntent.intent.setPackage("com.android.chrome");
            customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            customTabsIntent.launchUrl(context, uri);
            Log.d(TAG, "Opened with Custom Tabs");
        } catch (Exception openBrowserException) {
            Log.e(TAG, "Failed to open URL with a browser", openBrowserException);
            throw new RuntimeException("Cannot open browser", openBrowserException);
        }
    }
}
