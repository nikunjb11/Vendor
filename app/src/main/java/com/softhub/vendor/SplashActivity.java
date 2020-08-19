package com.softhub.vendor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    private Session session;
    private final int SPLASH_DISPLAY_LENGTH = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        session = new Session(getApplicationContext());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(session.loggedIn()){
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(i);
                    SplashActivity.this.finish();
                }

                else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    SplashActivity.this.startActivity(i);
                    SplashActivity.this.finish();
                    }
            }
        },SPLASH_DISPLAY_LENGTH);

    }
}
