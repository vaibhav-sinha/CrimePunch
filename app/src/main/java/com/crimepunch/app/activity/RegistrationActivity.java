package com.crimepunch.app.activity;

import android.content.Intent;
import android.widget.Toast;
import com.crimepunch.app.R;
import com.crimepunch.app.base.BaseActivity;
import com.crimepunch.app.config.Constants;
import com.crimepunch.app.helper.Session;

import android.os.Bundle;
import com.digits.sdk.android.*;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import io.fabric.sdk.android.Fabric;

import javax.inject.Inject;


public class RegistrationActivity extends BaseActivity {

    @Inject
    Session session;

    private AuthCallback authCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.TWITTER_KEY, Constants.TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());

        authCallback = new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_LONG).show();
                startProfileActivity(phoneNumber);
            }

            @Override
            public void failure(DigitsException exception) {
                Toast.makeText(getBaseContext(), "Failure", Toast.LENGTH_LONG).show();
            }
        };

        setContentView(com.crimepunch.app.R.layout.activity_registration);

        DigitsAuthButton digitsButton = (DigitsAuthButton) findViewById(R.id.auth_button);
        digitsButton.setCallback(authCallback);

        if(session.getUser(this) != null) {
            startHomeActivity();
        }
    }

    private void startProfileActivity(String phoneNumber) {
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("phoneNumber", phoneNumber);
        startActivity(i);
        finish();
    }

    private void startHomeActivity() {
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
        finish();
    }

}
