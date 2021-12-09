package com.example.developeiros;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class ActSplash extends AppCompatActivity
{
    private static final int SPLASH_SCREEN_TIME = 2500; // 2.5 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(
            () -> startActivity(new Intent(getApplicationContext(), ActLogin.class)),
            SPLASH_SCREEN_TIME
        );
    }
}