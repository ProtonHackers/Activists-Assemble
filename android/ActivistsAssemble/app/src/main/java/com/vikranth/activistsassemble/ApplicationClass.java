package com.vikranth.activistsassemble;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class ApplicationClass extends Application {
    private static final String TWITTER_KEY = "oYQi5QIlM2BU9dv9GO9FvbXxD";
    private static final String TWITTER_SECRET = "UMXwUTpDMK7t8hGmOcJqTzTZihR62UWZb1cmWvosyHTcpnVYTY";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
    }
}
